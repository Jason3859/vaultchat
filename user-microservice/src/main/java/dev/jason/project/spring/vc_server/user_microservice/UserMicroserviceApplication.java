package dev.jason.project.spring.vc_server.user_microservice;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import dev.jason.project.spring.vc_server.core.FirebaseConfig;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
@EnableFeignClients
public class UserMicroserviceApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(UserMicroserviceApplication.class, args);
        FirebaseConfig.initFirebase();
    }
}
