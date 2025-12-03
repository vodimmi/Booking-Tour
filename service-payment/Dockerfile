FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace
COPY pom.xml .
COPY service-auth ./service-auth
COPY service-tour ./service-tour
COPY service-booking ./service-booking
COPY service-payment ./service-payment
RUN mvn -B -DskipTests clean package -pl service-tour -am

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/service-tour/target/service-tour-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/app/app.jar"]
