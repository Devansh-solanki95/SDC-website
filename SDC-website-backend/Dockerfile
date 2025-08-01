# Stage 1: build the jar using Maven
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# copy pom and source, then build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: run the jar
FROM eclipse-temurin:17-jre
WORKDIR /app

# copy the built jar (assumes one jar in target)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
