package dev.jason.project.spring.vc_server.microservice.messaging;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import dev.jason.project.spring.vc_server.core.FirebaseConfig;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class MessagingApplication {

	static void main(String[] args) throws IOException {
		SpringApplication.run(MessagingApplication.class, args);
		FirebaseConfig.initFirebase();
	}
}
