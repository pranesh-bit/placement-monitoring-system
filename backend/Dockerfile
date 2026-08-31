# ── Stage 1: Build ────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy pom.xml first (for dependency caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build the jar
COPY src src
RUN mvn package -DskipTests -B

# ── Stage 2: Run ──────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar
COPY --from=build /app/target/*.jar app.jar

# Railway injects PORT env variable
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
