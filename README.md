# Booking-Tour

## Project Overview

Booking-Tour is a microservices-based Java application using Spring Boot 3.5.6 and Java 17. Each service follows a consistent structure for maintainability and scalability.

## How to Start the Project

1. **Clone the repository:**
   ```bash
   git clone <repo-url>
   cd Booking-Tour
   ```
2. **Start all services using Docker Compose:**

   ```bash
   docker-compose up --build
   ```

   This will build and start all microservices defined in `docker-compose.yml`.

   ```bash
   docker-compose up -d
   ```

   This command runs the services in detached mode.

   **CLI for creating the database (if needed):**

   - If using Flyway migrations (recommended), the database will be auto-created and migrated when the service starts.
   - To manually create the database (PostgreSQL example):
     ```bash
     docker exec -it <db-container-name> psql -U <db-username> -c "CREATE DATABASE <db-name>;"
     ```
   - For MySQL:
     ```bash
     docker exec -it <db-container-name> mysql -u <db-username> -p -e "CREATE DATABASE <db-name>;"
     ```
   - Replace `<db-container-name>`, `<db-username>`, and `<db-name>` with actual values.
   - Example for the auth service using MySQL:
     ```bash
     docker exec -it bt-mysql mysql -uroot -proot -e \\n  "CREATE USER 'admin'@'%' IDENTIFIED BY '123456'; \\n   GRANT ALL PRIVILEGES ON BT_AUTH.* TO 'admin'@'%'; \\n   FLUSH PRIVILEGES;"
     ```

   **CLI for running a single service locally (for development):**

   - Build and run with Maven:
     ```bash
     cd service-auth
     mvn spring-boot:run
     ```
   - Or build a JAR and run:
     ```bash
     mvn clean package
     java -jar target/*.jar
     ```

3. **Access services:**
   - Each service will be available on its configured port (see `docker-compose.yml`).
   - API documentation (Swagger/OpenAPI) is available if enabled in each service.

## Configuration Guide

- **application.yml:** Main configuration file for each service (database, JWT, ports, etc.).
- **Database migration:** Use Flyway scripts in `src/main/resources/db/migration/` for schema setup.
- **Environment variables:** Set sensitive configs (DB password, JWT secret) via environment variables or Docker secrets.

## Folder Structure Purpose

- `service-auth/` (example service)
  - `pom.xml`: Maven build file for the service.
  - `src/main/java/com/example/auth/`: Main source code.
    - `config/`: Spring and security configuration classes.
    - `controller/`: REST API controllers.
    - `dto/`: Data Transfer Objects for requests/responses.
    - `entity/`: JPA entities (database models).
    - `exception/`: Global and custom exception handlers.
    - `repository/`: Spring Data JPA repositories.
    - `security/`: JWT and authentication logic.
    - `service/`: Business logic and service classes.
  - `src/main/resources/`: Service configuration and resources.
    - `application.yml`: Service configuration file.
    - `db/migration/`: Flyway migration scripts.
  - `src/test/java/`: Unit and integration tests.
  - `target/`: Maven build output (auto-generated).

## Team Guideline: Setting Up Other Services

- **Create a new service:**
  1. Copy the structure of `service-auth` and rename appropriately (e.g., `service-booking`).
  2. Update `pom.xml` for the new service (artifactId, dependencies).
  3. Adjust package names and main class (e.g., `BookingServiceApplication.java`).
  4. Implement domain-specific logic in respective folders (`controller`, `service`, `entity`, etc.).
  5. Add the new service to `docker-compose.yml` for orchestration.
- **Maintain consistency:**
  - Follow the same folder and package structure for all services.
  - Use shared configuration patterns and code style.
  - Document API endpoints and configs in each service's README if needed.

## Contribution

- Follow branch naming conventions and pull request guidelines.
- Write unit/integration tests for new features.
- Keep documentation up to date.

---

For questions or onboarding, contact the project maintainer.
