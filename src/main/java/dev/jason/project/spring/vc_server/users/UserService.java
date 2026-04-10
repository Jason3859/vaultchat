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
        getUserEntityOrThrow(otherUser.uid()); // to check if other user exists

        if (entity.connections().contains(otherUser.uid())) {
            return;
        }

        entity.connections().add(otherUser.uid());

        repository.save(entity);
    }

    public void block(String uid, String dmUid) throws UserNotFoundException, UserAlreadyBlockedException {
        UserEntity entity = getUserEntityOrThrow(uid);
        getUserEntityOrThrow(dmUid); // to check of other user exists

        if (entity.blocklist().contains(dmUid)) {
            throw new UserAlreadyBlockedException();
        }

        entity.blocklist().add(dmUid);
        repository.save(entity);
    }

    public void unblock(String uid, String dmUid) throws UserNotFoundException, UserNotBlockedException, NoUsersBlockedException {
        UserEntity entity = getUserEntityOrThrow(uid);
        getUserEntityOrThrow(dmUid);
        List<String> blocklist = entity.blocklist();

        if (blocklist.isEmpty()) {
            throw new NoUsersBlockedException();
        }

        if (blocklist.contains(dmUid)) {
            blocklist.remove(dmUid);
            repository.save(entity);
        } else throw new UserNotBlockedException();
    }

    public void updateToken(String uid, String token, Device device) throws UserNotFoundException, DeviceNotFoundException {
        UserEntity entity = getUserEntityOrThrow(uid);
        List<Device> devices = entity.devices();

        for (Device d : devices) {
            if (d.equals(device)) {
                d.setFcmToken(token);
                repository.save(entity);
                return;
            }
        }

        throw new DeviceNotFoundException();
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
