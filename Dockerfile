# Use Maven with Java 21 as build stage
FROM maven:3.9.9-ibm-semeru-21-jammy AS build

# Set working directory
WORKDIR /app

# Copy project configuration files first
# This allows better dependency caching
COPY pom.xml .
COPY /src ./src

# Perform project build
# Skipping tests to speed up build process
RUN mvn clean package -DskipTests

# Use OpenJDK 21 slim image for runtime
FROM openjdk:24-ea-21-slim-bullseye

# Set working directory in final container
WORKDIR /app

# Copy JAR generated in build stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Command to start the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]