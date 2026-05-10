package dev.jason.project.spring.vc_server.microservice.messaging.repo.messaging;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.model.Device;

@Repository
@Profile("dev-fb") // fb here stands for firebase
public class MessagingRepoImpl implements MessagingRepository {
	
    @Override
    public void sendMessage(dev.jason.project.spring.vc_server.core.model.Message message, Device device, boolean forDeviceCheck) {
        Message firebaseMessage = Message.builder()
            .setToken(device.getToken())
            .putData("type", forDeviceCheck ? "check" : "message")
            .putAllData(message.asMap())
            .build();

        sendMessage(firebaseMessage);
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
    		// TODO: remove device from db
		}
    }
}
