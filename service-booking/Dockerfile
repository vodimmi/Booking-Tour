# Build stage
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /workspace
# Copy parent pom first
COPY pom.xml .
# Copy all service modules to allow for dependencies between them
COPY service-auth ./service-auth
COPY service-tour ./service-tour
COPY service-booking ./service-booking
# Build the application
RUN mvn -B -DskipTests clean package -pl service-booking -am

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/service-booking/target/service-booking-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]