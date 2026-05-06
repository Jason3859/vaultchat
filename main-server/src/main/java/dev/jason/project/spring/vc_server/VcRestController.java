package dev.jason.project.spring.vc_server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main Server REST Controller - API Gateway Entry Point
 * This server acts as an API Gateway that routes requests to microservices
 */
@RestController
public class VcRestController {
	
	@GetMapping
	public String home() {
		return "Hello, World!";
	}
}
