package dev.jason.project.spring.vc_server.microservice.messaging.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.model.User.Status;
import dev.jason.project.spring.vc_server.microservice.messaging.service.MessagingService;

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
	
	@PostMapping("/notify-status")
	public void notifyStatus(@RequestParam String uid, @RequestParam Status status) {
		messagingService.sendUserStatusUpdate(uid, status);
	}
}
