package dev.jason.project.spring.vc_server.microservice.messaging.repo.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.User;

@Repository
@Profile("firebase")
public class MessagingRepoImpl implements MessagingRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(MessagingRepoImpl.class);
	
    @Override
    public void sendMessage(dev.jason.project.spring.vc_server.core.model.Message message, Device device, boolean forDeviceCheck) {
        Message firebaseMessage = Message.builder()
            .setToken(device.getToken())
            .putData("type", forDeviceCheck ? "check" : "message")
            .putAllData(message.asMap())
            .setAndroidConfig(
            	AndroidConfig.builder()
            		.setPriority(Priority.HIGH)
            		.build()
            )
            .build();

        sendMessage(firebaseMessage);
        
        logger.info("sent message {} to {}", message.text(), device);
    }

    @Override
    public void sendUserStatusUpdate(Device device, String uid, User.Status status) {
        Message firebaseMessage = Message.builder()
            .setToken(device.getToken())
            .putData("type", "status_update")
            .putData("uid", uid)
            .putData("status", status.toString())
            .build();

        sendMessage(firebaseMessage);
    }

	private void sendMessage(Message message) {
    	try {
    		FirebaseMessaging.getInstance().send(message);
    	} catch (FirebaseMessagingException e) {
    		logger.error("Error occurred while sending message: {}", e.getMessage());
    		e.printStackTrace(System.err);
		}
    }
}
