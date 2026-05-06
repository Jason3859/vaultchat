package dev.jason.project.spring.vc_server.microservice.messaging.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.microservice.messaging.client.DeviceClient;
import dev.jason.project.spring.vc_server.microservice.messaging.client.SocialClient;
import dev.jason.project.spring.vc_server.microservice.messaging.client.UserClient;
import dev.jason.project.spring.vc_server.microservice.messaging.exception.MessagingException.MessageTextBlankException;
import dev.jason.project.spring.vc_server.microservice.messaging.repo.MessagingRepository;

@Service
public class MessagingService {
	
	@Autowired
	protected MessagingRepository repository;
	
	@Autowired
	private UserClient userClient;
	
	@Autowired
	private SocialClient socialClient;
	
	@Autowired
	private DeviceClient deviceClient;

	public void sendMessage(Message message) {
		sendMessage(message, false);
	}
	
	public void sendMessage(Message message, boolean forDeviceCheck) {
		if (message.text().isBlank()) {
			throw new MessageTextBlankException();
		}
		
		User from = userClient.getUserById(message.from()).asUser();
		User to = userClient.getUserById(message.to()).asUser();
		
		socialClient.connect(from.uid(), to.uid());
		
		List<Device> devices = deviceClient.getDevicesByOwner(to.uid()).stream()
			.map(deviceDto -> deviceDto.toDevice(null))
			.toList();
		
		if (devices.isEmpty()) {
			socialClient.addMessageToQueue(to.uid(), message);
			return;
		}
		
		devices.forEach(device -> {
			repository.sendMessage(message, device, forDeviceCheck);
		});
	}
	
	public void sendUserStatusUpdate(Device device, String uid, User.Status status) {
		repository.sendUserStatusUpdate(device, uid, status);
	}
}
