package dev.jason.project.spring.vc_server.device_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
public class DeviceMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceMicroserviceApplication.class, args);
    }

}
