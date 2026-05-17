package dev.jason.project.spring.vc_server.user_microservice;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import dev.jason.project.spring.vc_server.core.exception.VcExceptionHandler;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
@EnableFeignClients
@Import(VcExceptionHandler.class)
public class UserMicroserviceApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(UserMicroserviceApplication.class, args);
    }
}
