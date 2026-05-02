package dev.jason.project.spring.vc_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jason.project.spring.vc_server.model.Message;
import dev.jason.project.spring.vc_server.service.MessagingService;

@RestController
@RequestMapping("/messaging")
public class MessagingController {

	@Autowired
	private MessagingService messagingService;
	
	@PostMapping("/send")
	public ResponseEntity<Void> send(@RequestBody Message message) {
		messagingService.sendMessage(message);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
