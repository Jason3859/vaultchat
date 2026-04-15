package dev.jason.project.spring.vc_server.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import dev.jason.project.spring.vc_server.data.db.user.UserEntity;
import dev.jason.project.spring.vc_server.data.db.user.UserRepository;
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
        repository.save(entity);
        if (entity.status() == UserStatus.Offline) {
            updateUserStatusAndNotify(uid, UserStatus.Online);
        }
    }

    @Scheduled(fixedRate = 30000, initialDelay = 2000) // Check every 30 seconds
    public void checkStaleHeartbeats() {
        long now = System.currentTimeMillis();
        repository.findAll().stream()
            .filter(user -> user.status() != UserStatus.Offline) // takes online users
            .filter(user -> (now - user.lastHeartbeat()) >= 30000) // users of last heart beat less than 30 seconds
            .forEach(user -> {
                try {
                	updateUserStatusAndNotify(user.uid(), UserStatus.Offline);
                } catch (UserNotFoundException ignored) {
                }
            });
    }

    public void saveUser(User user) throws DeviceAlreadyExistsException {
        UserEntity entity = repository.findByUid(user.uid());

        if (entity == null) {
            repository.save(UserEntity.fromDomainUser(user));
        } else {
            Device device = user.devices().getFirst();
            if (!entity.devices().contains(device)) {
                entity.devices().add(device);
                repository.save(entity);
            } else throw new DeviceAlreadyExistsException();
        }
    }

    @SuppressWarnings("null")
    public void addConnection(User user, User otherUser) throws UserNotFoundException {
        UserEntity entity1 = getUserEntityOrThrow(user.uid());
        UserEntity entity2 =  getUserEntityOrThrow(otherUser.uid());

        if (entity1.connections().contains(otherUser.uid())) {
            return;
        }

        entity1.connections().add(otherUser.uid());
        entity2.connections().add(user.uid());

        repository.saveAll(List.of(entity1, entity2));
    }

    @SuppressWarnings("null")
	public void block(String uid1, String uid2) throws UserNotFoundException, UserAlreadyBlockedException {
        UserEntity entity1 = getUserEntityOrThrow(uid1);
        UserEntity entity2 = getUserEntityOrThrow(uid2);

        if (entity1.blocklist().contains(uid2)) {
            throw new UserAlreadyBlockedException();
        }

        entity1.blocklist().add(uid2);
        entity1.connections().remove(uid2);

        entity2.blocklist().add(uid1);
        entity2.connections().remove(uid1);

        repository.saveAll(List.of(entity1, entity2));
    }

    @SuppressWarnings("null")
	public void unblock(String uid1, String uid2) throws UserNotFoundException, UserNotBlockedException, NoUsersBlockedException {
        UserEntity entity1 = getUserEntityOrThrow(uid1);
        UserEntity entity2 = getUserEntityOrThrow(uid2);

        if (entity1.blocklist().isEmpty()) {
            throw new NoUsersBlockedException();
        }

        if (entity1.blocklist().contains(uid2)) {
            entity1.blocklist().remove(uid2);
            entity2.blocklist().remove(uid1);

            entity1.connections().add(entity2.uid());
            entity2.connections().add(entity1.uid());

            repository.saveAll(List.of(entity1, entity2));
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
