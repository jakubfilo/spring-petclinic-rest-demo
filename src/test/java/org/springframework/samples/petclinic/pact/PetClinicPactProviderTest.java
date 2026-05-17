package org.springframework.samples.petclinic.pact;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;

@Provider("petclinic-rest")
@Consumer("petclinic-consumer")
@IgnoreNoPactsToVerify
@PactBroker(url = "${pactbroker.url:https://pact-broker-app.gentlefield-825c3ee5.polandcentral.azurecontainerapps.io}",
		authentication = @PactBrokerAuth(username = "${pactbroker.auth.username}", password = "${pactbroker.auth.password}"))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "petclinic.security.enable=false")
/*
 you need to add environemnt variables into junit configuration

 In IntelliJ, right-click your test class → Run 'PetClinicPactProviderTest'
 It will create a temporary run configuration.
 Open Run → Edit Configurations…
 Find your JUnit test configuration
 Add environment variables:
 */
public class PetClinicPactProviderTest {

	@LocalServerPort
	private int port;

	@MockitoBean
	private ClinicService clinicService;

	@BeforeEach
	void setUp(PactVerificationContext context) {
		if (context != null) {
			context.setTarget(new HttpTestTarget("localhost", port, "/petclinic"));
		}
	}

	@TestTemplate
	@ExtendWith(PactVerificationSpringProvider.class)
	void verifyPact(PactVerificationContext context) {
		if (context != null) {
			context.verifyInteraction();
		}
	}

	@State("vet with id 1 exists and has specialties")
	void getVet_vetExistsWithSpecialties() {
		Specialty radiology = Specialty.builder()
				.id(1)
				.name("radiology")
				.build();

		when(clinicService.findVetById(1)).thenReturn(
				Vet.builder()
						.id(1)
						.firstName("James")
						.lastName("Carter")
						.specialties(Set.of(radiology))
						.build()
		);
	}

	@State("vet with id 1 exists and can be deleted")
	void deleteVet_deletesExistingVet() {
		when(clinicService.findVetById(1)).thenReturn(
				Vet.builder()
						.id(1)
						.firstName("James")
						.lastName("Carter")
						.specialties(Set.of())
						.build()
		);
	}

	@State("vet with id 1 exists")
	void updateVet_updatesExistingVet() {
		when(clinicService.findVetById(1)).thenReturn(
				Vet.builder()
						.id(1)
						.firstName("James")
						.lastName("Carter")
						.specialties(new HashSet<>())
						.build()
		);
	}

	@State("new vets can be created without specialties")
	void addVet_createsVetWithNoSpecialties() {
		doAnswer(invocation -> {
			Vet vet = invocation.getArgument(0);
			vet.setId(11);
			return null;
		}).when(clinicService).saveVet(any());
	}

	@State("specialty with id 1 named surgery exists and new vets can be created")
	void addVet_createsVetWithSpecialties() {
		Specialty surgery = Specialty.builder()
				.id(1)
				.name("surgery")
				.build();

		when(clinicService.findSpecialtiesByNameIn(any())).thenReturn(List.of(surgery));
		doAnswer(invocation -> {
			Vet vet = invocation.getArgument(0);
			vet.setId(10);
			return null;
		}).when(clinicService).saveVet(any());
	}

	@State("no vet exists with id 999")
	void getVet_vetNotFound() {
		when(clinicService.findVetById(999)).thenReturn(null);
	}

	@State("vet with id 2 exists and has no specialties")
	void getVet_vetExistsWithNoSpecialties() {
		when(clinicService.findVetById(2)).thenReturn(
				Vet.builder()
						.id(2)
						.firstName("Helen")
						.lastName("Leary")
						.specialties(Set.of())
						.build()
		);
	}

	@State("at least one vet exists without specialties")
	void listVets_vetWithNoSpecialtiesInList() {
		when(clinicService.findAllVets()).thenReturn(List.of(
				Vet.builder()
						.id(2)
						.firstName("Helen")
						.lastName("Leary")
						.specialties(Set.of())
						.build()
		));
	}

	@State("no vets exist in the system")
	void listVets_returnsEmptyList() {
		when(clinicService.findAllVets()).thenReturn(List.of());
	}

	@State("at least one vet exists with specialties")
	void listVets_returnsVetsWithSpecialties() {
		Specialty radiology = Specialty.builder()
				.id(1)
				.name("radiology")
				.build();

		when(clinicService.findAllVets()).thenReturn(List.of(
				Vet.builder()
						.id(1)
						.firstName("James")
						.lastName("Carter")
						.specialties(Set.of(radiology))
						.build()
		));
	}

}
