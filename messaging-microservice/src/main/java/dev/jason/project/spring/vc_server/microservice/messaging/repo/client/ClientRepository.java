package dev.jason.project.spring.vc_server.microservice.messaging.repo.client;

import java.util.List;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.model.User;

public interface ClientRepository {
    
    List<Device> getDevicesByOwner(String uid);
    void verifyDevice(Device device);

    void connect(String uid1, String uid2);
    void addMessageToQueue(String uid, Message message);
	List<User> getConnections(String uid);
	boolean getIsUserBlocked(String uid1, String uid2);

    User getUserById(String uid);
}
