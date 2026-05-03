package dev.jason.project.spring.vc_server.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.VcServerApplication;
import dev.jason.project.spring.vc_server.exception.VcException.BlockedByUserException;
import dev.jason.project.spring.vc_server.exception.VcException.DeviceAlreadyExistsException;
import dev.jason.project.spring.vc_server.exception.VcException.DeviceNotFoundException;
import dev.jason.project.spring.vc_server.exception.VcException.NoUsersBlockedException;
import dev.jason.project.spring.vc_server.exception.VcException.SelfBlockException;
import dev.jason.project.spring.vc_server.exception.VcException.SelfUnblockException;
import dev.jason.project.spring.vc_server.exception.VcException.UserAlreadyBlockedException;
import dev.jason.project.spring.vc_server.exception.VcException.UserAlreadyExistsException;
import dev.jason.project.spring.vc_server.exception.VcException.UserNotBlockedException;
import dev.jason.project.spring.vc_server.exception.VcException.UserNotFoundException;
import dev.jason.project.spring.vc_server.model.Device;
import dev.jason.project.spring.vc_server.model.Message;
import dev.jason.project.spring.vc_server.model.User;
import dev.jason.project.spring.vc_server.repo.db.user.UserEntity;
import dev.jason.project.spring.vc_server.repo.db.user.UserRepository;

@Service
public class UserService {

    @Autowired
    protected UserRepository repository;

    @Autowired
    private MessagingService messagingService;

    // --- User Management ---
    
    public void addAdminToDB() {
    	UserEntity entity = UserEntity.fromDomainUser(VcServerApplication.ADMIN_USER);
    	
    	boolean exists = repository.findByUid(entity.uid()) != null;
    	
    	if (!exists) {
			repository.save(entity);
		}
    }

    public void addUser(User user) {
        UserEntity entity = repository.findByUid(user.uid());

        if (entity == null) {
            repository.save(UserEntity.fromDomainUser(user));
        } else {
            throw new UserAlreadyExistsException();
        }
    }
    
    public void deleteUser(String uid) {
    	UserEntity entity = getUserEntityOrThrow(uid);
    	repository.delete(entity);
    }

    public User getUserByUid(String uid) {
        return getUserEntityOrThrow(uid).toDomainUser();
    }

    public List<User> getAllUsersByDisplayName(String name) {
        return repository.findByDisplayNameContainingIgnoreCase(name).stream()
        	.map(UserEntity::toDomainUser)
        	.filter(user -> !user.equals(VcServerApplication.ADMIN_USER))
        	.toList();
    }
    
    // --- Device Management ---
    
    public void addDevice(String uid, Device device) {
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

    public void deleteDevice(String uid, Device device) {
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

    public void updateToken(String uid, String token, Device device) {
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
    
    // --- Social and Connections ---
    
    public void addConnection(String uid1, String uid2) {
        UserEntity entity1 = getUserEntityOrThrow(uid1);
        UserEntity entity2 =  getUserEntityOrThrow(uid2);

        if (entity1.connections().contains(uid2)) {
            return;
        }
        
        // if user is blocked
        if (entity1.blocklist().contains(uid2) || entity2.blocklist().contains(uid1)) {
        	throw new BlockedByUserException();
        }

        entity1.connections().add(uid2);
        entity2.connections().add(uid1);

        repository.saveAll(List.of(entity1, entity2));
    }

    public void block(String uid1, String uid2) {
    	
    	if (Objects.equals(uid1, uid2)) {
			throw new SelfBlockException();
		}
    	
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

    public void unblock(String uid1, String uid2) {
    	
    	if (Objects.equals(uid1, uid2)) {
    		throw new SelfUnblockException();
    	}
    	
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
    
    // --- Heart beat ---
    
    public void updateHeartbeat(String uid) {
        UserEntity entity = getUserEntityOrThrow(uid);
        entity.setLastHeartbeat(System.currentTimeMillis());
        repository.save(entity);
        if (entity.status() == User.Status.Offline) {
            updateUserStatusAndNotify(uid, User.Status.Online);
        }
    }

    // --- Messaging ---

    public void addMessageToQueue(String uid, Message message) {
        UserEntity entity = getUserEntityOrThrow(uid);
        List<Message> queuedMessages = entity.queuedMessages();

        if (queuedMessages.contains(message)) return;

        queuedMessages.add(message);
        repository.save(entity);
    }

    // --- Helpers ---

    protected void updateUserStatusAndNotify(String uid, User.Status status) {
    	UserEntity entity = getUserEntityOrThrow(uid);
    	entity.setStatus(status);
    	repository.save(entity);
    	
    	// Notify connections
    	List<String> connections = entity.connections();
    	if (!connections.isEmpty()) {
    		connections.forEach(connectionUid -> {
    			try {
    				User connection = getUserByUid(connectionUid);
    				if (!connection.devices().isEmpty()) {
    					connection.devices().forEach(device -> {
    						messagingService.sendUserStatusUpdate(device, uid, status);
    					});
    				}
    			} catch (UserNotFoundException ignored) {
    			}
    		}); 
    	}
    }

    protected UserEntity getUserEntityOrThrow(String uid) {
        UserEntity entity = repository.findByUid(uid);

        if (entity == null) {
            throw new UserNotFoundException();
        }

        return entity;
    }
}
