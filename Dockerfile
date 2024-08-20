# Use an official Gradle image as a base
FROM gradle:8.8-jdk17 as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and project files to the container
COPY --chown=gradle:gradle . .

# Build the Spring Boot application
RUN gradle build --no-daemon

# Use a lighter base image to run the application
FROM openjdk:17-jdk-slim

# # Set the working directory for the application
WORKDIR /app

# # Copy the built jar from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# # Expose the port your app runs on
EXPOSE 8080

# # Run the Spring Boot application
ENTRYPOINT ["java","-jar","/app/app.jar"]
