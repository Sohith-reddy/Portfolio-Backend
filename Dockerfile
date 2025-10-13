# Use official OpenJDK 17 image
FROM openjdk:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the fat JAR into container
COPY target/PortfolioBackend-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render will map dynamically)
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
