package dev.jason.project.spring.vc_server.microservice.messaging.repo.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.model.User.Status;

@Repository
public class MessagingRepoNoFirebaseImpl implements MessagingRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(MessagingRepoNoFirebaseImpl.class);

	@Override
	public void sendMessage(Message message, Device device, boolean forDeviceCheck) {
		logger.info("sent message {} to device {}", message, device);
	}

	@Override
	public void sendUserStatusUpdate(Device device, String uid, Status status) {
		logger.info("sent status update to device {} about user {}, status {}", device, uid, status);
	}
}
