# Use JDK 21
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy maven wrapper and pom first
COPY mvnw pom.xml ./
COPY .mvn .mvn

# FIX: give execute permission
RUN chmod +x mvnw

# Copy source
COPY src src

# Build jar
RUN ./mvnw clean package -DskipTests

# Expose Spring Boot port
EXPOSE 8080

# Run app
CMD ["java","-jar","target/DisasterAlert-0.0.1-SNAPSHOT.jar"]
