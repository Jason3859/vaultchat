# ================================
# Stage 1: Build
# ================================
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

# Copy Gradle wrapper and config
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# Give execute permission to gradlew
RUN chmod +x gradlew

# Download dependencies first (better Docker cache)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build the jar
RUN ./gradlew bootJar --no-daemon -x test

# ================================
# Stage 2: Run
# ================================
FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]