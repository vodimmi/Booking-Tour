Service Payment (PayPal) - Standalone microservice
====================================================

Overview
--------
- Project name: service-payment
- Purpose: create and store payment records, simulate PayPal flow (create approval URL and execute)
- DB name default: BT_PAYMENT (configurable via environment)

What is included
-----------------
- Spring Boot app (REST)
- Flyway migration to create payments table
- Dockerfile + pom.xml
- Minimal simulated PayPal flow (replace with real SDK calls when ready)
- Optional: if you set BOOKING_SERVICE_URL env var, you can call booking service to update booking after payment (not implemented by default).

How to add into your existing monorepo (project root where docker-compose.yml lives)
----------------------------------------------------------------------------------

1. Copy the `service-payment` folder into your project root (side-by-side with service-tour, service-booking, etc.).

2. Update your top-level `pom.xml` (if you use multimodule) OR just let Dockerfile build the module by including it in COPY steps. Example top-level Docker build (like your existing setup):
   - Add `COPY service-payment ./service-payment` into the build stage of the main Dockerfile used for building artifacts (or modify to build payment separately).

3. Add service to docker-compose.yml (example):

services:
  service-payment:
    build:
      context: .
      dockerfile: service-payment/Dockerfile
    container_name: bt-service-payment
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/BT_PAYMENT?allowPublicKeyRetrieval=true&useSSL=false
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_ENABLED: "true"
      SPRING_FLYWAY_BASELINE_ON_MIGRATE: "true"
      SPRING_FLYWAY_LOCATIONS: classpath:db/migration
      SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT: org.hibernate.dialect.MySQL8Dialect
      SERVER_PORT: 8090
    ports:
      - "8090:8090"
    depends_on:
      mysql:
        condition: service_healthy

4. Create the BT_PAYMENT database in your mysql init scripts or manually. Example init SQL (add to your mysql-init folder):

CREATE DATABASE IF NOT EXISTS BT_PAYMENT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

5. Build & run via docker-compose:
   - If you use a top-level builder image that compiles modules, ensure service-payment is included in COPY and built.
   - Alternatively you can build the module separately:
     cd service-payment
     mvn -DskipTests clean package
     (then run docker-compose up)

Pay flow (simulated)
--------------------
1. POST /api/payments/create  -> {bookingId, amount, currency}
   returns approvalUrl and orderId (fake). Frontend should redirect user to approvalUrl (in real PayPal integration, you would redirect to PayPal sandbox URL).

2. After buyer approves on PayPal, frontend/backend should call POST /api/payments/execute?orderId=ORDER-... to mark payment as COMPLETED.
   When payment is COMPLETED you can call booking service to set booking status to CONFIRMED (OPTIONAL).

Next steps to integrate real PayPal
----------------------------------
- Add PayPal Java SDK dependency and implement Order creation and capture flows using client id / secret
- Secure endpoints, add signatures verification for webhooks, switch to HTTPS in production

Contact
-------
If you want, I can:
- replace simulated parts with real PayPal SDK code and sample env vars (CLIENT_ID, CLIENT_SECRET)
- add webhook endpoint that verifies PayPal signatures
- add a call to service-booking to update booking status automatically after success (requires service-booking URL)
