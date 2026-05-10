package dev.jason.project.spring.vc_server.microservice.messaging.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.okhttp.OkHttpClient;

@Configuration
public class FeignConfig {

	@Bean
	OkHttpClient okHttpClient() {
		return new OkHttpClient();
	}
}
