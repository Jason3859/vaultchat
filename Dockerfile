FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/vc_server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]