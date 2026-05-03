package dev.jason.project.spring.vc_server.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.VcServerApplication;
import dev.jason.project.spring.vc_server.model.Message;

@Service
public class DeviceService extends UserService {
	
	@Autowired
	private MessagingService messagingService;

	@Scheduled(initialDelay = 2000, fixedRate = 30000) // check every 30s
	public void sendMessage() { // if device if reported unregistered by firebase, device will be removed
		String uid = VcServerApplication.ADMIN_USER.uid();
		
		repository.findAll().forEach(user -> {
			Message message = new Message(uid, user.uid(), "hai", LocalDateTime.now().toString());
			messagingService.sendMessage(message, true);
		});
	}
}
