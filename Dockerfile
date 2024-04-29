# Use a Maven base image to handle the Maven build
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .

# Fetch all dependencies
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean install -DskipTests

# Build the runtime image
FROM openjdk:22-ea-21-slim
VOLUME /tmp
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]