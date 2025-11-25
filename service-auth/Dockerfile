# =======================
# Build stage
# =======================
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Copy toàn bộ project (để Maven có thể thấy parent pom)
COPY . .

# Build service-auth
RUN mvn -B -DskipTests clean package -pl service-auth -am

# =======================
# Run stage
# =======================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/service-auth/target/service-auth-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 6060
ENTRYPOINT ["java", "-jar", "app.jar"]
