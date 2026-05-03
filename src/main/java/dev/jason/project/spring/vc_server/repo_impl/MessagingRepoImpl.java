package dev.jason.project.spring.vc_server.repo_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;

import dev.jason.project.spring.vc_server.exception.VcException.MessagingException;
import dev.jason.project.spring.vc_server.model.Device;
import dev.jason.project.spring.vc_server.model.User;
import dev.jason.project.spring.vc_server.repo.messaging.MessagingRepository;
import dev.jason.project.spring.vc_server.service.UserService;

@Repository
public class MessagingRepoImpl implements MessagingRepository {
	
	@Autowired
	private UserService userService;
	
    @Override
    public void sendMessage(dev.jason.project.spring.vc_server.model.Message message, Device device, boolean forDeviceCheck) {
        Message firebaseMessage = Message.builder()
            .setToken(device.token())
            .putData("type", forDeviceCheck ? "check" : "message")
            .putAllData(message.asMap())
            .build();

        sendMessage(firebaseMessage, message.to(), device);
    }

    @Override
    public void sendUserStatusUpdate(Device device, String uid, User.Status status) {
        Message firebaseMessage = Message.builder()
            .setToken(device.token())
            .putData("type", "status_update")
            .putData("uid", uid)
            .putData("status", status.toString())
            .build();

        sendMessage(firebaseMessage, uid, device);
    }

	private void sendMessage(Message message, String uid, Device device) {
    	try {
    		FirebaseMessaging.getInstance().send(message);
    	} catch (FirebaseMessagingException e) {
    		MessagingErrorCode errorCode = e.getMessagingErrorCode();
    		
    		switch (errorCode) {
    			case UNREGISTERED -> {
    				userService.deleteDevice(uid, device);
    			}
    			
				default -> {
					e.printStackTrace(System.err);
					throw new MessagingException();
				}
    		}
		}
    }
}
