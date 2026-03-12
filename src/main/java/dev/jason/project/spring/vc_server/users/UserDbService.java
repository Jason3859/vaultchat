package dev.jason.project.spring.vc_server.users;

import dev.jason.project.spring.vc_server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDbService {

    @Autowired
    UserDbRepository repository;

    public void saveUser(DBUser dbUser) {
        repository.save(dbUser);
    }

    public List<User> getAllUsers() {
        return repository.findAll().stream().map(DBUser::toDomainUser).toList();
    }

    public User getUserByUid(String uid) {
        return repository.findByUid(uid).toDomainUser();
    }

    public List<User> getUsersByDisplayName(String displayName) {
        return repository.findByDisplayName(displayName).stream()
                .map(DBUser::toDomainUser)
                .toList();
    }

    public String getUserFcmTokenByUid(String uid) {
        return repository.findByUid(uid).fcmToken();
    }

    public void updateUserFcmToken(String uid, String fcmToken) {
        User existingUser = getUserByUid(uid);
        repository.save(new DBUser(existingUser.uid(), existingUser.displayName(), existingUser.profilePictureUrl(), fcmToken));
    }
}
