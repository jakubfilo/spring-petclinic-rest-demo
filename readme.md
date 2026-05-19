# Spring PetClinic REST — Demo Fork

This repository is a fork of [spring-petclinic/spring-petclinic-rest](https://github.com/spring-petclinic/spring-petclinic-rest). All credit for the original application goes to the Spring PetClinic community and its contributors.

The original codebase contained discrepancies between the controller implementations and the provided OpenAPI specification. These have been corrected so that the running application accurately reflects the contract described in `src/main/resources/openapi.yml`. The compliance fixes and the Pact provider tests were AI-generated: the fixes by analysing the spec against the existing controllers, and the provider tests by supplying consumer Pact definitions as context.

## Purpose

This fork serves as a demo provider application for two API governance workflows:

- **oasdiff GitHub Action** — automated OpenAPI diff and breaking-change detection on pull requests.
- **Pact provider tests** — contract verification ensuring the provider honours consumer-driven contracts published to a Pact Broker.

## Running the application

### Maven

```sh
git clone https://github.com/jakubfilo/spring-petclinic-rest-demo.git
cd spring-petclinic-rest-demo
./mvnw spring-boot:run
```

### Docker

```sh
docker run -p 9966:9966 springcommunity/spring-petclinic-rest
```

The application is available at [http://localhost:9966/petclinic/](http://localhost:9966/petclinic/).

## API documentation

Swagger UI: [http://localhost:9966/petclinic/swagger-ui.html](http://localhost:9966/petclinic/swagger-ui.html)

OpenAPI 3.1 spec: [http://localhost:9966/petclinic/v3/api-docs](http://localhost:9966/petclinic/v3/api-docs)

## API endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| **Owners** | | |
| GET | `/api/owners` | Retrieve all pet owners |
| GET | `/api/owners/{ownerId}` | Get a pet owner by ID |
| POST | `/api/owners` | Add a new pet owner |
| PUT | `/api/owners/{ownerId}` | Update an owner's details |
| DELETE | `/api/owners/{ownerId}` | Delete an owner |
| GET | `/api/owners/{ownerId}/pets/{petId}` | Get a pet by ID (owner's pet) |
| PUT | `/api/owners/{ownerId}/pets/{petId}` | Update pet details (owner's pet) |
| POST | `/api/owners/{ownerId}/pets` | Add a new pet to an owner |
| POST | `/api/owners/{ownerId}/pets/{petId}/visits` | Add a vet visit for a pet |
| **Pets** | | |
| GET | `/api/pets` | Retrieve all pets |
| GET | `/api/pets/{petId}` | Get a pet by ID |
| PUT | `/api/pets/{petId}` | Update pet details |
| DELETE | `/api/pets/{petId}` | Delete a pet |
| **Vets** | | |
| GET | `/api/vets` | Retrieve all veterinarians |
| GET | `/api/vets/{vetId}` | Get a vet by ID |
| POST | `/api/vets` | Add a new vet |
| PUT | `/api/vets/{vetId}` | Update vet details |
| DELETE | `/api/vets/{vetId}` | Delete a vet |
| **Pet Types** | | |
| GET | `/api/pettypes` | Retrieve all pet types |
| GET | `/api/pettypes/{petTypeId}` | Get a pet type by ID |
| POST | `/api/pettypes` | Add a new pet type |
| PUT | `/api/pettypes/{petTypeId}` | Update pet type details |
| DELETE | `/api/pettypes/{petTypeId}` | Delete a pet type |
| **Specialties** | | |
| GET | `/api/specialties` | Retrieve all vet specialties |
| GET | `/api/specialties/{specialtyId}` | Get a specialty by ID |
| POST | `/api/specialties` | Add a new specialty |
| PUT | `/api/specialties/{specialtyId}` | Update a specialty |
| DELETE | `/api/specialties/{specialtyId}` | Delete a specialty |
| **Visits** | | |
| GET | `/api/visits` | Retrieve all vet visits |
| GET | `/api/visits/{visitId}` | Get a visit by ID |
| POST | `/api/visits` | Add a new visit |
| PUT | `/api/visits/{visitId}` | Update a visit |
| DELETE | `/api/visits/{visitId}` | Delete a visit |
| **Users** | | |
| POST | `/api/users` | Create a new user |

## License and attribution

This project is based on work by the [Spring PetClinic](https://github.com/spring-petclinic) community, licensed under the Apache License 2.0. See the original repository for full license details and contributor history.