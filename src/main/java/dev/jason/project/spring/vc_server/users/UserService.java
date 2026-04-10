package dev.jason.project.spring.vc_server.users;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import dev.jason.project.spring.vc_server.domain.Device;
import dev.jason.project.spring.vc_server.domain.Logger;
import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.domain.UserStatus;
import dev.jason.project.spring.vc_server.domain.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public void updateHeartbeat(String uid) throws UserNotFoundException {
        UserEntity entity = getUserEntityOrThrow(uid);
        entity.setLastHeartbeat(System.currentTimeMillis());
        if (entity.status() == UserStatus.Offline) {
            updateUserStatusAndNotify(uid, UserStatus.Online);
        } else {
            repository.save(entity);
        }
    }

    @Scheduled(fixedRate = 30000) // Check every 30 seconds
    public void checkStaleHeartbeats() {
        long now = System.currentTimeMillis();
        repository.findAll().stream()
            .filter(user -> user.status() != UserStatus.Offline)
            .filter(user -> (now - user.lastHeartbeat()) > 60000)
            .forEach(user -> {
                try {
                    updateUserStatusAndNotify(user.toDomainUser().uid(), UserStatus.Offline);
                } catch (UserNotFoundException ignored) {
                }
            });
    }

    public void saveUser(User user) throws UserAlreadyExistsException {
        UserEntity entity = repository.findByUid(user.uid());

        if (entity == null) {
            repository.save(UserEntity.fromDomainUser(user));
        } else {
            throw new UserAlreadyExistsException();
        }
    }

    public void addConnection(User user, User otherUser) throws UserNotFoundException {
        UserEntity entity = getUserEntityOrThrow(user.uid());
        List<String> connections = new ArrayList<>(List.of());

        try {
            if (user.connections().contains(otherUser.uid())) return;
            user.connections().add(otherUser.uid());
            connections.addAll(user.connections());
        } catch (NullPointerException ignored) {
            connections.add(otherUser.uid());
        }

        entity.setConnections(connections);
        repository.save(entity);
    }

    public void addBlocklist(String uid, String dmUid) {
        UserEntity entity = repository.findByUid(uid);

        List<String> blocklist = new ArrayList<>(List.of());

        try {
            if (entity.blocklist().contains(dmUid)) return;
            entity.blocklist().add(dmUid);
            blocklist.addAll(entity.blocklist());
        } catch (NullPointerException ignored) {
            blocklist.add(dmUid);
        }

        entity.setBlocklist(blocklist);
        repository.save(entity);
    }

    public void unblock(String uid, String dmUid) throws VcException {
        UserEntity user = getUserEntityOrThrow(uid);
        getUserEntityOrThrow(dmUid);
        List<String> blocklist = user.blocklist();

        if (blocklist == null) {
            throw new NoUsersBlockedException();
        }

        if (blocklist.contains(dmUid)) {
            blocklist.remove(dmUid);
            user.setBlocklist(blocklist);
            repository.save(user);
        } else throw new UserNotBlockedException();
    }

    public List<User> getAllUsersByDisplayName(String name) {
        return repository.findByDisplayNameContainingIgnoreCase(name).stream().map(UserEntity::toDomainUser).toList();
    }

    public List<Device> getUserDevicesByUid(String uid) throws UserNotFoundException {
        return getUserEntityOrThrow(uid).devices();
    }

    public User getUserByUid(String uid) {
        try {
            return getUserOrThrow(uid);
        } catch (Exception e) {
            return null;
        }
    }

    public User getUserOrThrow(String uid) throws UserNotFoundException {
        return getUserEntityOrThrow(uid).toDomainUser();
    }

    private UserEntity getUserEntityOrThrow(String uid) throws UserNotFoundException {
        UserEntity entity = repository.findByUid(uid);

        if (entity == null) {
            throw new UserNotFoundException();
        }

        return entity;
    }

    public void addDevice(String uid, Device device) throws UserNotFoundException, DeviceAlreadyExistsException {
        UserEntity entity = getUserEntityOrThrow(uid);
        List<Device> devices = entity.devices();

        for (Device d : devices) {
            if (d.equals(device)) {
                throw new DeviceAlreadyExistsException();
            }
        }

        entity.devices().add(device);
        repository.save(entity);
    }

    public void deleteDevice(String uid, Device device) throws UserNotFoundException, DeviceNotFoundException {
        UserEntity entity = getUserEntityOrThrow(uid);
        List<Device> devices = entity.devices();

        for (Device d : devices) {
            if (d.equals(device)) {
                devices.remove(d);
                repository.save(entity);
                return;
            }
        }

        throw new DeviceNotFoundException();
    }

    public void updateUserStatusAndNotify(String uid, UserStatus status) throws UserNotFoundException {
        UserEntity entity = getUserEntityOrThrow(uid);
        entity.setStatus(status);
        repository.save(entity);

        // Notify connections
        List<String> connections = entity.toDomainUser().connections();
        if (connections != null) {
            for (String connectionUid : connections) {
                try {
                    User connection = getUserOrThrow(connectionUid);
                    if (connection.devices() != null) {
                        for (Device device : connection.devices()) {
                            Message message = Message.builder()
                                .setToken(device.fcmToken())
                                .putData("type", "status_update")
                                .putData("uid", uid)
                                .putData("status", status.name())
                                .build();
                            FirebaseMessaging.getInstance().send(message);
                        }
                    }
                } catch (UserNotFoundException | FirebaseMessagingException e) {
                    Logger.write(e);
                }
            }
        }
    }
}
