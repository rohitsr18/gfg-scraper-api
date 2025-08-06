# Stage 1: Build the application with Maven
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw && ./mvnw clean install

# Stage 2: Create a minimal runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Use a wildcard to copy the JAR. This handles any version.
COPY --from=builder /app/target/*.jar app.jar

# Run the renamed JAR file
CMD ["java", "-jar", "app.jar"]