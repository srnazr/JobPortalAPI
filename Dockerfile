FROM eclipse-temurin:17-jdk as build

WORKDIR /app

# Copy maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (this layer will be cached)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# Runtime stage
ENV SPRING_PROFILES_ACTIVE=prod
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/job-portal-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application with more verbose logging
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--logging.level.root=INFO", "--logging.level.com.serena.jobportal=DEBUG"]