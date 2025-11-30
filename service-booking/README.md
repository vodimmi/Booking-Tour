# Booking Service

This project is a Spring Boot application for managing bookings. It provides a RESTful API to create, retrieve, update, and delete bookings.

## Project Structure

```
service-booking
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── booking
│       │               ├── BookingApplication.java
│       │               ├── controller
│       │               │   └── BookingController.java
│       │               ├── dto
│       │               │   ├── request
│       │               │   │   └── BookingRequest.java
│       │               │   └── response
│       │               │       └── BookingResponse.java
│       │               ├── entity
│       │               │   └── Booking.java
│       │               ├── exception
│       │               │   ├── BookingException.java
│       │               │   └── GlobalExceptionHandler.java
│       │               ├── repository
│       │               │   └── BookingRepository.java
│       │               └── service
│       │                   ├── BookingService.java
│       │                   └── impl
│       │                       └── BookingServiceImpl.java
│       └── resources
│           ├── application.yml
│           └── db
│               └── migration
│                   └── V1__init_booking.sql
├── Dockerfile
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd service-booking
   ```

2. **Build the project:**
   ```bash
   ./mvnw clean install
   ```

3. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the API:**
   The API will be available at `http://localhost:8082/api/bookings`.

## API Endpoints

- **Create Booking:** `POST /api/bookings`
- **Get Booking:** `GET /api/bookings/{id}`
- **Update Booking:** `PUT /api/bookings/{id}`
- **Delete Booking:** `DELETE /api/bookings/{id}`
- **Get All Bookings:** `GET /api/bookings`

## Technologies Used

- Spring Boot
- JPA/Hibernate
- MySQL
- Docker

## License

This project is licensed under the MIT License.