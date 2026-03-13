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

    public void saveUser(DBUser dbUser) {
        repository.save(dbUser);
    }

    public void addConnection(String uid, String dmUid) {
        DBUser user = repository.findByUid(uid);

        List<String> connections = new ArrayList<>(Arrays.asList(user.connections()));
        connections.add(dmUid);
        String[] connectionsArray = connections.toArray(new String[0]);

        DBUser updatedUser = new DBUser(user.uid(), user.displayName(), user.profilePictureUrl(), user.fcmToken(), connectionsArray);
        repository.save(updatedUser);
    }

    public List<User> getAllUsersByDisplayName(String name) {
        return repository.findByDisplayNameContainingIgnoreCase(name).stream().map(DBUser::toDomainUser).toList();
    }

    public String getUserFcmTokenByUid(String uid) {
        return repository.findByUid(uid).fcmToken();
    }

    public void updateUserFcmToken(String uid, String fcmToken) {
        DBUser existingUser = repository.findByUid(uid);
        repository.save(new DBUser(existingUser.uid(), existingUser.displayName(), existingUser.profilePictureUrl(), fcmToken, existingUser.connections()));
    }
}
