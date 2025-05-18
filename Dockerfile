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
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/job-portal-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Set MongoDB and JWT environment variables as build arguments with defaults
ENV MONGODB_URI=mongodb+srv://serenazaarour:serena552005@jobportalcluster.2sgvca5.mongodb.net/?retryWrites=true&w=majority&appName=JobPortalCluster
ENV MONGODB_DATABASE=JobPortalDB
ENV JWT_SECRET=YourVeryLongSecretKeyHereThatIsAtLeast256BitsLongForSecureJWTSigning
ENV JWT_EXPIRATION=86400000
ENV JWT_ISSUER=JobPortalAPI
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]