## Use official OpenJDK 17 image
#FROM openjdk:17-jdk-alpine
#
## Set working directory
#WORKDIR /app
#
## Copy the fat JAR into container
#COPY target/PortfolioBackend-0.0.1-SNAPSHOT.jar app.jar
#
## Expose port (Render will map dynamically)
#EXPOSE 8080
#
## Run the JAR
#ENTRYPOINT ["java", "-jar", "app.jar"]

# -------- Stage 1: Build the app --------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# -------- Stage 2: Run the app --------
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy only the built JAR from the previous stage
COPY --from=build /app/target/PortfolioBackend-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render will assign dynamically)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
