
# Dogs API

A RESTful API for managing and searching dogs. The application provides endpoints for creating, retrieving, updating, and filtering dogs, along with lookup endpoints for metadata such as gender, status, and leaving reasons.

Built with **Java**, **Micronaut**, **Gradle**, and **Flyway**.

---

# Features

- Create and manage dog records
- Search and filter dogs using flexible query parameters
- Lookup endpoints for enum values (gender, status, leaving reasons)
- Database migrations with Flyway
- DTO-based API responses
- Unit tests
- Logging with Logback

---

# Tech Stack

- Java
- Micronaut
- Gradle
- Flyway

---

# Project Structure

```
src/main/java/com/polaris
│
├── controller
│   ├── DogController.java
│   └── DogLookupController.java
│
├── service
│   └── DogService.java
│
├── repository
│   ├── DogRepository.java
│   ├── DogSearchRepository.java
│   └── DogSearchRepositoryImpl.java
│
├── model
│   ├── dto
│   │   ├── DogRequest.java
│   │   ├── DogResponse.java
│   │   ├── DogFilter.java
│   │   └── LookupResponse.java
│   │
│   ├── entity
│   │   ├── Dog.java
│   │   ├── DogGender.java
│   │   ├── DogStatus.java
│   │   └── DogLeavingReason.java
│   │
│   └── mapper
│       └── DogMapper.java
│
├── exception
│   ├── DogNotFoundException.java
│   └── DogNotFoundExceptionHandler.java
│
└── Application.java
```

---

# API Endpoints

## Dogs

### Get all dogs

```
GET /api/dogs/dogs
```

Supports filtering via query parameters.

Example:

```
GET /api/dogs/dogs?filter=supplier:Charity A&includeDeleted=true
```
Refer to the filtering section for more detail

---

### Get dog by ID

```
GET /dogs/{id}
```

---

### Create a dog

```
POST /dogs
```

Example request body:

```json
{
  "name": "Max",
  "age": 4,
  "gender": "MALE",
  "status": "AVAILABLE"
}
```

---

### Update a dog

```
PUT /dogs/{id}
```

---

### Delete a dog

```
DELETE /dogs/{id}
```

---

# Lookup Endpoints

Provides values for dropdowns or filters.

### Dog Status

```
GET /lookup/status
```

### Dog Gender

```
GET /lookup/gender
```

### Leaving Reasons

```
GET /lookup/leaving-reasons
```

---

# Database

Database schema and seed data are managed via **Flyway migrations**.

```
src/main/resources/db/migration
```

### Migrations

```
V1__create_dogs.sql
V2__seed_dogs.sql
```

---

# Running the Application

## Requirements

- Java 17+
- Gradle

## Run locally

```
./gradlew run
```

The API will start on:

```
http://localhost:8080
```

---

# Running Tests

```
./gradlew test
```

---

# Logging

Logging is configured via:

```
src/main/resources/logback.xml
```

---

# Error Handling

Custom exception handling is implemented for cases such as:

```
DogNotFoundException
```

Handled globally by:

```
DogNotFoundExceptionHandler
```

---

# Example Response

```json
{
  "id": 1,
  "name": "Bella",
  "age": 3,
  "gender": "FEMALE",
  "status": "AVAILABLE"
}
```

---

# Future Improvements

- Pagination support for dog search
- Authentication and authorization
- OpenAPI / Swagger documentation
- Docker containerization
- Advanced search filters
