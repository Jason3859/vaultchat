package dev.jason.project.spring.vc_server.repo_impl;

import org.springframework.stereotype.Repository;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import dev.jason.project.spring.vc_server.exception.VcException.MessagingException;
import dev.jason.project.spring.vc_server.model.Device;
import dev.jason.project.spring.vc_server.model.User;
import dev.jason.project.spring.vc_server.repo.messaging.MessagingRepository;

@Repository
public class MessagingRepoImpl implements MessagingRepository {
    @Override
    public void sendMessage(dev.jason.project.spring.vc_server.model.Message message, Device device) {
        Message firebaseMessage = Message.builder()
            .setToken(device.token())
            .putData("type", "message")
            .putAllData(message.asMap())
            .build();

        sendMessage(firebaseMessage);
    }

    @Override
    public void sendUserStatusUpdate(Device device, String uid, User.Status status) {
        Message firebaseMessage = Message.builder()
            .setToken(device.token())
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
			e.printStackTrace(System.err);
			throw new MessagingException();
		}
    }
}
