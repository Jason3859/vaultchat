package dev.jason.project.spring.vc_server.domain.service;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.domain.exception.DeviceAlreadyExistsException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.model.Device;
import dev.jason.project.spring.vc_server.domain.model.Message;
import dev.jason.project.spring.vc_server.domain.model.Result;
import dev.jason.project.spring.vc_server.domain.model.User;
import dev.jason.project.spring.vc_server.domain.repo.db.user.UserEntity;
import dev.jason.project.spring.vc_server.domain.repo.db.user.UserRepository;
import dev.jason.project.spring.vc_server.domain.repo.messaging.MessagingRepository;

@Service
public class UserService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private MessagingRepository messagingRepository;

    // --- User Management ---

    public void saveUser(User user) throws DeviceAlreadyExistsException {
        UserEntity entity = userRepository.findByUid(user.uid());

        if (entity == null) {
            userRepository.save(UserEntity.fromDomainUser(user));
        } else {
            Device device = user.devices().getFirst();
            boolean doesDeviceExist = entity.devices().contains(device);
            
			if (!doesDeviceExist) {
                entity.devices().add(device);
                userRepository.save(entity);
            } else {
            	throw new DeviceAlreadyExistsException();
            }
        }
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

    public List<User> getAllUsersByDisplayName(String name) {
        return userRepository.findByDisplayNameContainingIgnoreCase(name).stream().map(UserEntity::toDomainUser).toList();
    }

    // --- Messaging ---

    public void addMessageToQueue(String uid, Message message) throws UserNotFoundException {
        UserEntity entity = getUserEntityOrThrow(uid);
        List<Message> queuedMessages = entity.queuedMessages();

        if (queuedMessages.isEmpty()) return;
        if (queuedMessages.contains(message)) return;

        queuedMessages.add(message);
        userRepository.save(entity);
    }

    // --- Helpers ---

    public void updateUserStatusAndNotify(String uid, User.Status status) throws UserNotFoundException {
    	UserEntity entity = getUserEntityOrThrow(uid);
    	entity.setStatus(status);
    	userRepository.save(entity);
    	
    	// Notify connections
    	List<String> connections = entity.connections();
    	if (!connections.isEmpty()) {
    		connections.forEach(connectionUid -> {
    			try {
    				User connection = getUserOrThrow(connectionUid);
    				if (!connection.devices().isEmpty()) {
    					connection.devices().forEach(device -> {
    						Result result = messagingRepository.sendUserStatusUpdate(device, uid, status);
    						
    						if (result != Result.Success) {
    							logger.error("Error result while sending user status update");
    						}
    					});
    				}
    			} catch (UserNotFoundException ignored) {
    			}
    		}); 
    	}
    }

    protected UserEntity getUserEntityOrThrow(String uid) throws UserNotFoundException {
        UserEntity entity = userRepository.findByUid(uid);

        if (entity == null) {
            throw new UserNotFoundException();
        }

        return entity;
    }
}
