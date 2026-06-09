package dev.jason.project.spring.vc_server.microservice.messaging.repo.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.microservice.messaging.client.DeviceClient;
import dev.jason.project.spring.vc_server.microservice.messaging.client.SocialClient;
import dev.jason.project.spring.vc_server.microservice.messaging.client.UserClient;

@Repository
public class ClientRepoImpl implements ClientRepository {

    @Autowired
    private DeviceClient deviceClient;

    @Autowired
    private SocialClient socialClient;

    @Autowired
    private UserClient userClient;

    @Override
    public void addMessageToQueue(String uid, Message message) {
        socialClient.addMessageToQueue(uid, message);
    }

    @Override
    public void connect(String uid1, String uid2) {
        socialClient.connect(uid1, uid2);
    }

    @Override
    public List<User> getConnections(String uid) {
        return socialClient.getConnections(uid).stream()
            .map(u -> u.asUser())
            .toList();
    }

    @Override
    public List<Device> getDevicesByOwner(String uid) {
        return deviceClient.getDevicesByOwner(uid).stream()
            .map(d -> d.toDevice(null))
            .toList();
    }
    
    @Override
    public boolean getIsUserBlocked(String uid1, String uid2) {
    	return socialClient.isUserBlocked(uid1, uid2);
    }

    @Override
    public User getUserById(String uid) {
        return userClient.getUserById(uid).asUser();
    }

	@Override
	public void verifyDevice(Device device) {
		deviceClient.verifyDevice(DeviceDto.asDto(device));
	}
}
