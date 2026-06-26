# ================================
# Stage 1: Build all services
# ================================
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

# Copy all service folders
COPY device-microservice ./device-microservice
COPY messaging-microservice ./messaging-microservice
COPY social-microservice ./social-microservice
COPY user-microservice ./user-microservice
COPY main-server ./main-server

# Build each service
RUN cd device-microservice && chmod +x gradlew && ./gradlew bootJar --no-daemon -x test
RUN cd messaging-microservice && chmod +x gradlew && ./gradlew bootJar --no-daemon -x test
RUN cd social-microservice && chmod +x gradlew && ./gradlew bootJar --no-daemon -x test
RUN cd user-microservice && chmod +x gradlew && ./gradlew bootJar --no-daemon -x test
RUN cd main-server && chmod +x gradlew && ./gradlew bootJar --no-daemon -x test

# ================================
# Stage 2: Run all services
# ================================
FROM eclipse-temurin:25-jdk

# Install supervisord to run multiple processes
RUN apt-get update && apt-get install -y supervisor && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy all built jars
COPY --from=builder /app/device-microservice/build/libs/*.jar device-microservice.jar
COPY --from=builder /app/messaging-microservice/build/libs/*.jar messaging-microservice.jar
COPY --from=builder /app/social-microservice/build/libs/*.jar social-microservice.jar
COPY --from=builder /app/user-microservice/build/libs/*.jar user-microservice.jar
COPY --from=builder /app/main-server/build/libs/*.jar main-server.jar

# Supervisord config to start all 5 services
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

EXPOSE 8080

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]