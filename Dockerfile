# --- STAGE 1: Build all modules ---
FROM gradle:9.4.1-jdk25-alpine AS builder
WORKDIR /app

# Cache gradle layers
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle

# Copy build configurations
COPY core/build.gradle ./core/
COPY device-microservice/build.gradle ./device-microservice/
COPY main-server/build.gradle ./main-server/
COPY messaging-microservice/build.gradle ./messaging-microservice/
COPY social-microservice/build.gradle ./social-microservice/
COPY user-microservice/build.gradle ./user-microservice/

# Copy source code
COPY core/src ./core/src
COPY device-microservice/src ./device-microservice/src
COPY main-server/src ./main-server/src
COPY messaging-microservice/src ./messaging-microservice/src
COPY social-microservice/src ./social-microservice/src
COPY user-microservice/src ./user-microservice/src

RUN chmod +x ./gradlew
RUN ./gradlew :main-server:bootJar :device-microservice:bootJar :messaging-microservice:bootJar :social-microservice:bootJar :user-microservice:bootJar --no-daemon

# --- STAGE 2: Low-Memory JRE Runtime using Temurin 25 ---
FROM eclipse-temurin:25-jdk-alpine AS runtime
WORKDIR /app

# Argument to target a specific microservice per deployment
ARG MODULE_NAME
ENV MODULE=${MODULE_NAME}

# Copy any generated jar file from the build folder into the runtime directory
COPY --from=builder /app/${MODULE_NAME}/build/libs/*.jar ./

# Aggressive memory optimization for 512MB RAM constraints
ENV JAVA_OPTS="-Xms150m -Xmx280m -XX:+UseSerialGC -Xss256k -XX:MaxMetaspaceSize=80m"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar $(ls *.jar | head -n 1)  --spring.profiles.active=prod"]
