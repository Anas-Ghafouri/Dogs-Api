
# Dogs API

A RESTful API for managing and searching dogs. The application provides endpoints for creating, retrieving, updating, 
and filtering dogs, along with lookup endpoints for gender, status, and leaving reason.

---
# Quick Start

Clone the repository:

git clone https://github.com/Anas-Ghafouri/dogs-api.git

cd dogs-api

Run the application:

./gradlew run

The API will be available at:

http://localhost:8080

---
# Features

- Create and manage dog records
- Search and filter dogs using flexible query parameters
- Lookup endpoints for enum values (gender, status, leaving reason)
- Database migrations and seed data using Flyway
- DTO-based API responses
- A number of unit tests
- Logging with Logback

---

# Tech Stack

- Java
- Micronaut
- Gradle
- Flyway
- H2 Embedded Database

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

# Data Model
The core domain entity in this application is Dog, which represents a dog managed by the system.
It is mapped to the dogs table and includes both operational fields and audit fields to support lifecycle tracking 
and soft deletion.

| Column | Type | Description |
|------|------|-------------|
| id | Long | Primary key for the dog record. Automatically generated using database identity strategy. |
| name | String | Name of the dog. This field is required and cannot be null. |
| breed | String | Breed of the dog. |
| supplier | String | Organisation or source from which the dog was acquired. |
| badge_id | String | Internal reference or badge identifier used to track the dog within the system. |
| gender | DogGender (Enum) | Gender of the dog. Stored as a string in the database. |
| birth_date | LocalDate | Date of birth of the dog. |
| date_acquired | LocalDate | Date the dog was acquired by the organisation. |
| current_status | DogStatus (Enum) | Current status of the dog within the system (for example available, adopted, etc.). |
| leaving_date | LocalDate | Date the dog left the organisation. |
| leaving_reason | DogLeavingReason (Enum) | Reason the dog left the organisation (for example adoption, transfer, etc.). Stored as a string enum. |
| kennelling_characteristic | String | Notes describing any special kennelling requirements or behavioural characteristics. |
| deleted | boolean | Soft delete flag. Indicates whether the record has been logically deleted. Defaults to false. |
| deleted_at | Instant | Timestamp indicating when the dog record was soft deleted. |
| created_at | Instant | Timestamp indicating when the record was created. Automatically populated when the entity is persisted. |
| updated_at | Instant | Timestamp indicating when the record was last updated. Automatically updated whenever the entity is modified. |
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
GET /api/dogs/{id}
```

---

### Create a dog

```
POST /api/dogs
```

Example request body:

```json
{
  "name": "Curly",
  "breed": "Labrador Retriever",
  "supplier": "Charity B",
  "badgeId": "B0011",
  "gender": "FEMALE",
  "birthDate": "2018-05-11",
  "dateAcquired": "2020-01-11",
  "currentStatus": "IN_TRAINING",
  "kennellingCharacteristic": "Calm and friendly"
}
```

---

### Update a dog

```
PUT /api/dogs/{id}
```

---

### Delete a dog

```
DELETE /api/dogs/{id}
```

---

# Lookup Endpoints

Provides values for status, gender and leaving reason.

### Dog Status

```
GET /api/dogs/lookups/statuses
```

### Dog Gender

```
GET /api/dogs/lookups/genders
```

### Leaving Reasons

```
GET /api/dogs/lookups/leaving-reasons
```
### Base URL

By default the API runs on:

http://localhost:8080

---

# Filtering

Filtering is implemented using a custom repository that dynamically builds SQL predicates based on the provided filters.

Dogs can be filtered using the filter query parameter, which takes a key value input separated by a comma.

Example:

````
GET /api/dogs/dogs?filter=supplier:Charity A,breed:Labrador Retriever
````
The filter uses SQL-like wildcard matching.. The filter query parameter also validates basic errors such as omitting 
the colon, blank filter values and passing an unsupported filter field.

# Pagination

The GET api/dogs/dogs endpoint supports pagination to limit the number of results returned in a single response.

### Query Parameters

| Parameter | Description |
|-----------|-------------|
| page | The page number to retrieve (starting from 0) |
| size | The number of records to return per page |

### Example Request

````
GET /api/dogs/dogs?page=0&size=5
````
This request returns the first 5 dogs.

### Example Request With Filtering
Pagination can be combined with filtering.
````
GET /api/dogs/dogs?filter=supplier:Charity A&page=1&size=2
````
This request returns the second page of dogs where the supplier is Charity A and each page contains only 2 elements.

### Response Structure
Example:

````json
{
  "content": [
    {
      "id": 7,
      "name": "Shadow",
      "breed": "Border Collie",
      "supplier": "Charity A",
      "badgeId": "B007",
      "gender": "MALE",
      "birthDate": "2019-01-01",
      "dateAcquired": "2020-08-20",
      "currentStatus": "IN_SERVICE",
      "kennellingCharacteristic": "Very intelligent",
      "deleted": false,
      "createdAt": "2026-03-09T15:49:50.771542Z",
      "updatedAt": "2026-03-09T15:49:50.771542Z"
    },
    {
      "id": 11,
      "name": "Rex1",
      "breed": "Labrador Retriever1",
      "supplier": "Charity A1",
      "badgeId": "B0011",
      "gender": "FEMALE",
      "birthDate": "2018-05-11",
      "dateAcquired": "2020-01-11",
      "currentStatus": "IN_TRAINING",
      "kennellingCharacteristic": "Calm and friendly",
      "deleted": false,
      "createdAt": "2026-03-09T15:50:38.931630Z",
      "updatedAt": "2026-03-09T15:50:38.931630Z"
    }
  ],
  "pageable": {
    "size": 2,
    "number": 1,
    "sort": {},
    "mode": "OFFSET"
  },
  "totalSize": 4
}
````

# Soft Delete
The API implements a soft delete strategy.

Instead of permanently deleting a dog record, the following fields were added to the Dog entity:
*	deleted (boolean)
*	deletedAt (timestamp)
*	updatedAt (timestamp)

When a dog is deleted:
*	deleted is set to true
*	deletedAt stores the deletion timestamp

Soft deleted dogs:
*	Are excluded from normal queries
*	Can optionally be included using the includeDeleted=true query parameter (when requesting the list of all dogs)



# Database

Database schema and seed data are managed by **Flyway**. The project includes a seed file to populate the database with initial dog data for development and testing purposes.

This allows the API to be used immediately after startup without requiring manual data entry.

The relevant files are in the following directory:
```
src/main/resources/db/
```

### Migrations

```
V1__create_dogs.sql
```

### Seed File

```
R__seed_dogs.sql
```


---


# Running the Application

## Requirements

- Java 21
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
The project includes limited unit tests covering key service and controller functionality.

These tests demonstrate the testing structure and provide basic coverage of core features.

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
Invalid requests (for example malformed parameters or invalid input) will result in standard HTTP error responses such as:
*	400 Bad Request
*	404 Not Found
---

### Example Response

```json
{
  "error": "Invalid Filter",
  "message": "Invalid filter format 'supplier:'. Filter value must not be blank."
}
```

---

# Future Enhancements

The following improvements could further enhance the system’s functionality, scalability, and maintainability.

### API Features

- **Sorting support for filtered search results**  
  Allow clients to specify sorting parameters in addition to filters when retrieving dog records.

- **Advanced filtering capabilities**  
  Extend the current filtering system to support more complex queries such as:
    - date range filtering (e.g. `birthDate`, `dateAcquired`)
    - combination filters using logical operators

### Documentation

- **OpenAPI documentation**  
  Add automated API documentation using OpenAPI to provide interactive endpoint exploration and improve developer experience.

### Infrastructure

- **Production-ready database**  
  Replace the embedded H2 database with a production-grade database such as PostgreSQL.

- **Database indexing**  
  Add indexes on frequently queried fields to improve search performance as data volume increases.