# --- Stage 1: build ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

# --- Stage 2: run ---
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/lab1-book-library.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
