# ================================
# Stage 1: Build all services
# ================================
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

# Copy everything from root
COPY . .

# Give execute permission to gradlew
RUN chmod +x gradlew

# Build all services from root
RUN ./gradlew :main-server:bootJar :device-microservice:bootJar :messaging-microservice:bootJar :social-microservice:bootJar :user-microservice:bootJar --no-daemon -x test

# ================================
# Stage 2: Run all services
# ================================
FROM eclipse-temurin:25-jdk

RUN apt-get update && apt-get install -y supervisor && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy each built jar
COPY --from=builder /app/main-server/build/libs/*.jar main-server.jar
COPY --from=builder /app/device-microservice/build/libs/*.jar device-microservice.jar
COPY --from=builder /app/messaging-microservice/build/libs/*.jar messaging-microservice.jar
COPY --from=builder /app/social-microservice/build/libs/*.jar social-microservice.jar
COPY --from=builder /app/user-microservice/build/libs/*.jar user-microservice.jar

COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

EXPOSE 8080

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]