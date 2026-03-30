package dev.jason.project.spring.vc_server.users;

import dev.jason.project.spring.vc_server.domain.*;
import dev.jason.project.spring.vc_server.domain.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public void saveUser(UserEntity userEntity) throws UserAlreadyExistsException {
        UserEntity entity = repository.findByUid(userEntity.uid());

        if (entity == null) {
            repository.save(userEntity);
        } else throw new UserAlreadyExistsException();
    }

    public void addConnection(String uid, String dmUid) {
        UserEntity user = repository.findByUid(uid);

        String[] connectionsArray;

        try {
            List<String> connections = new ArrayList<>(Arrays.asList(user.connections()));

            if (connections.contains(dmUid)) return;

            connections.add(dmUid);
            connectionsArray = connections.toArray(new String[0]);
        } catch (NullPointerException ignored) {
            connectionsArray = new String[] { dmUid };
        }

        UserEntity updatedUser = new UserEntity(user.uid(), user.displayName(), user.profilePictureUrl(), user.fcmToken(), connectionsArray, user.blocklist());
        repository.save(updatedUser);
    }

    public void addBlocklist(String uid, String dmUid) {
        UserEntity user = repository.findByUid(uid);

        String[] blocklistArray;

        try {
            List<String> blocklist = new ArrayList<>(Arrays.asList(user.blocklist()));

            if (blocklist.contains(dmUid)) return;

            blocklist.add(dmUid);
            blocklistArray = blocklist.toArray(new String[0]);
        } catch (NullPointerException ignored) {
            blocklistArray = new String[] { dmUid };
        }

        UserEntity updatedUser = new UserEntity(user.uid(), user.displayName(), user.profilePictureUrl(), user.fcmToken(), user.connections(), blocklistArray);
        repository.save(updatedUser);
    }

    public void unblock(String uid, String dmUid) throws VcException {
        UserEntity user = getUserEntityOrThrow(uid);
        List<String> blocklist;

        try {
            blocklist = new ArrayList<>(Arrays.asList(user.blocklist()));
        } catch (NullPointerException ignored) {
            throw new NoUsersBlockedException();
        }

        if (blocklist.contains(dmUid)) {
            blocklist.remove(dmUid);

            String[] blocklistArray = blocklist.toArray(new String[0]);
            UserEntity updatedUser = new UserEntity(user.uid(), user.displayName(), user.profilePictureUrl(), user.fcmToken(), user.connections(), blocklistArray);
            repository.save(updatedUser);
        } else throw new UserNotBlockedException();
    }

    public List<String> getBlockedUserUidsByUserUid(String uid) {
        return Arrays.stream(repository.findByUid(uid).blocklist()).toList();
    }

    public List<User> getAllUsersByDisplayName(String name) {
        return repository.findByDisplayNameContainingIgnoreCase(name).stream().map(UserEntity::toDomainUser).toList();
    }

    public String getUserFcmTokenByUid(String uid) throws UserNotFoundException {
        return getUserEntityOrThrow(uid).fcmToken();
    }

    public User getUserByUid(String uid) {
        UserEntity entity = repository.findByUid(uid);

        return entity == null ? null : entity.toDomainUser();
    }

    public void updateUserFcmToken(String uid, String fcmToken) throws UserNotFoundException {
        UserEntity existingUser = getUserEntityOrThrow(uid);
        repository.save(new UserEntity(existingUser.uid(), existingUser.displayName(), existingUser.profilePictureUrl(), fcmToken, existingUser.connections(), existingUser.blocklist()));
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
}
