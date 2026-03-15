package dev.jason.project.spring.vc_server.users;

import dev.jason.project.spring.vc_server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserDbService {

    @Autowired
    UserDbRepository repository;

    public void saveUser(UserDbEntity userDbEntity) {
        repository.save(userDbEntity);
    }

    public void addConnection(String uid, String dmUid) {
        UserDbEntity user = repository.findByUid(uid);

        String[] connectionsArray;

        try {
            List<String> connections = new ArrayList<>(Arrays.asList(user.connections()));
            connections.add(dmUid);
            connectionsArray = connections.toArray(new String[0]);
        } catch (NullPointerException ignored) {
            connectionsArray = new String[] { dmUid };
        }

        UserDbEntity updatedUser = new UserDbEntity(user.uid(), user.displayName(), user.profilePictureUrl(), user.fcmToken(), connectionsArray);
        repository.save(updatedUser);
    }

    public List<User> getAllUsersByDisplayName(String name) {
        return repository.findByDisplayNameContainingIgnoreCase(name).stream().map(UserDbEntity::toDomainUser).toList();
    }

    public String getUserFcmTokenByUid(String uid) {
        return repository.findByUid(uid).fcmToken();
    }

    public void updateUserFcmToken(String uid, String fcmToken) {
        UserDbEntity existingUser = repository.findByUid(uid);
        repository.save(new UserDbEntity(existingUser.uid(), existingUser.displayName(), existingUser.profilePictureUrl(), fcmToken, existingUser.connections()));
    }
}
