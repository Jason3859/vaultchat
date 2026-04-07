package dev.jason.project.spring.vc_server.users;

import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.domain.UserFcmToken;
import dev.jason.project.spring.vc_server.domain.exception.NoUsersBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.exception.VcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public void saveUser(User user) {
    	try {
    		UserEntity entity = getUserEntityOrThrow(user.uid());
    		entity.fcmTokens().add(user.fcmTokens().getFirst());
    		repository.save(entity);
    	} catch (UserNotFoundException e) {
    		repository.save(UserEntity.fromDomainUser(user));			
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

    public List<UserFcmToken> getUserFcmTokensByUid(String uid) throws UserNotFoundException {
        return getUserEntityOrThrow(uid).fcmTokens();
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

    public void updateTokenLastUsed(String uid, UserFcmToken token) throws UserNotFoundException {
        UserEntity entity = getUserEntityOrThrow(uid);
        UserFcmToken newToken = new UserFcmToken(token.token(), LocalDateTime.now());

        entity.fcmTokens().removeIf(it -> it.token().equals(token.token()));
        entity.fcmTokens().add(newToken);

        repository.save(entity);
    }
    
    public void verifyUserOrThrow(String uid) throws UserNotFoundException {
    	getUserOrThrow(uid);
    }

	public List<User> getAllUsers() {
		return repository.findAll().stream()
			.map(UserEntity::toDomainUser)
			.toList();
	}
}
