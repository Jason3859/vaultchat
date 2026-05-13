package dev.jason.project.spring.vc_server.microservice.messaging.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.microservice.messaging.exception.MessagingException.BlockedByUserException;
import dev.jason.project.spring.vc_server.microservice.messaging.exception.MessagingException.MessageTextBlankException;
import dev.jason.project.spring.vc_server.microservice.messaging.repo.client.ClientRepository;
import dev.jason.project.spring.vc_server.microservice.messaging.repo.messaging.MessagingRepository;

@Service
public class MessagingService {
	
	@Autowired
	protected MessagingRepository repository;
	
	@Autowired
    private ClientRepository client;

	public void sendMessage(Message message) {
		sendMessage(message, false);
	}
	
	public void sendMessage(Message message, boolean forDeviceCheck) {
		if (message == null || message.text().isBlank()) {
			throw new MessageTextBlankException();
		}
		
		User from = client.getUserById(message.from());
		User to = client.getUserById(message.to());
		
		if (client.getIsUserBlocked(message.from(), message.to())) {
			throw new BlockedByUserException();
		}
		
		client.connect(from.uid(), to.uid());
		
		List<Device> devices = client.getDevicesByOwner(to.uid());
		
		if (devices.isEmpty()) {
			client.addMessageToQueue(to.uid(), message);
			return;
		}
		
		devices.forEach(device -> {
			repository.sendMessage(message, device, forDeviceCheck);
		});
	}
	
	public void sendUserStatusUpdate(String uid, User.Status status) {
		List<String> connections = client.getConnections(uid).stream()
			.map(u -> u.uid())
			.toList();
		
		connections.forEach(connectionUid -> {
			List<Device> devices = client.getDevicesByOwner(connectionUid);
			
			if (devices.isEmpty()) {
				return;
			}
			
			devices.forEach(device -> {
				repository.sendUserStatusUpdate(device, uid, status);
			});
		});
	}
}
