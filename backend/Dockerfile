# Build application
FROM maven:3.9.9-eclipse-temurin-24-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Second Stage: Image
FROM eclipse-temurin:24-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8484
CMD ["java", "-jar", "app.jar"]
