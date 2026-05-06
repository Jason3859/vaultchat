package dev.jason.project.spring.vc_server.social_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
@EnableFeignClients
public class SocialMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialMicroserviceApplication.class, args);
	}
}
