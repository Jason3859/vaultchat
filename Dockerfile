FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
WORKDIR /app/vaultchat-server
RUN chmod +x ../gradlew
RUN ../gradlew clean shadowJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/vaultchat-server/build/libs/vaultchat-server-0.0.1.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]