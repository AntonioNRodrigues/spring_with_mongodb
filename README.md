# spring_with_mongodb

A small Spring Boot example application that demonstrates how to upload files to MongoDB using GridFS and store simple metadata about documents in a MongoDB collection.

This project shows a minimal service with REST endpoints to:

- Upload a document and its metadata (title, author) and store the file in GridFS
- Retrieve the document metadata by ID
- Download the stored file by its GridFS file ID

The implementation uses Spring Boot (Web + Spring Data MongoDB) and GridFS via Spring Data's GridFsTemplate.

## Project structure

- `src/main/java` - application source
  - `tz.projects.controllers.DocumentController` - REST controllers for upload/download and metadata
  - `tz.projects.service.DocumentService` - application logic that stores files in GridFS and uses a repository for metadata
  - `tz.projects.entities.DocumentRecord` - metadata entity persisted in `documents` collection
  - `tz.projects.repositories.DocumentRecordRepository` - Spring Data Mongo repository
- `src/test/java` - unit tests

## Requirements

- Java 17+ (the project POM currently sets a modern Java source/target)
- Maven 3.6+
- A running MongoDB instance (local or via Docker) for integration scenarios. Unit tests are mocked and do not require a running DB.

## Quickstart

You can run this project either with a local MongoDB instance or with Docker Compose (provided).

### Option A — Run with Docker Compose (recommended for quick local testing)

1. Start MongoDB using the included `docker-compose.yml`:

   ```bash
   docker-compose up -d
   ```

2. Build and run the Spring Boot application with Maven:

   ```bash
   mvn -U -DskipTests package spring-boot:run
   ```

The application will read MongoDB host/port/database from `src/main/resources/application.yml`.

### Option B — Run against an existing MongoDB

1. Make sure MongoDB is reachable at the host/port configured in `application.yml`.
2. Build and run the app:

   ```bash
   mvn -U -DskipTests package spring-boot:run
   ```

## Running tests

Unit tests are located in `src/test/java`. They use JUnit 5 and Mockito. Run them with:

```bash
mvn test
```

## REST API

Base path: `/documents`

1. Upload a document (multipart/form-data)

- Method: POST
- URL: `/documents`
- Form fields:
  - `title` (string)
  - `author` (string)
  - `file` (file)

Example curl:

   ```bash
   curl -v -F "title=MyDoc" -F "author=Alice" -F "file=@/path/to/local/file.pdf" http://localhost:8080/documents
   ```

Response: JSON representation of the saved `DocumentRecord` (contains metadata and `fileId`).

2. Get document metadata by ID

- Method: GET
- URL: `/documents/{id}`

Response: 200 + JSON DocumentRecord when found, 404 when not.

Example curl:

   ```bash
   curl -v http://localhost:8080/documents/611a1b2c3d4e5f6a7b8c9d0e
   ```

3. Download a stored file by GridFS file ID

- Method: GET
- URL: `/documents/file/{fileId}`

Response: 200 + file as attachment with appropriate Content-Type, or 404 when not found.

Example curl:

   ```bash
   curl -v -OJ http://localhost:8080/documents/file/<fileId>
   ```

## Notes about GridFS

- Files are stored in MongoDB GridFS and referenced from `DocumentRecord.fileId`.
- The `DocumentService` uses `GridFsTemplate` to store and retrieve files.

## Development notes

- The unit tests in `src/test/java` mock the `DocumentService` or the GridFS operations; they do not require a live MongoDB instance.
- For end-to-end manual testing, use the Docker Compose file included in the project to spin up MongoDB.

## Contributing

Contributions, issue reports and improvements are welcome. Please follow the existing project style and add tests for new behavior.

## License

This example project is provided as-is for learning and demonstration purposes.
