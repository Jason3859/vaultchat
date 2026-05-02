package dev.jason.project.spring.vc_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.exception.VcException.MessageTextBlankException;
import dev.jason.project.spring.vc_server.model.Message;
import dev.jason.project.spring.vc_server.model.User;
import dev.jason.project.spring.vc_server.repo.messaging.MessagingRepository;

@Service
public class MessagingService {
	
	@Autowired
	private MessagingRepository messagingRepository;
	
	@Autowired
	private UserService userService;

	public void sendMessage(Message message) {
		if (message.text().isBlank()) {
			throw new MessageTextBlankException();
		}
		
		User from = userService.getUserByUid(message.from());
		User to = userService.getUserByUid(message.to());
		
		userService.addConnection(from.uid(), to.uid());
		
		if (to.devices().isEmpty()) {
			userService.addMessageToQueue(to.uid(), message);
			return;
		}
		
		to.devices().forEach(device -> {
			messagingRepository.sendMessage(message, device);
			System.out.println("sent message to device %s".formatted(device.name()));
		});
	}
}
