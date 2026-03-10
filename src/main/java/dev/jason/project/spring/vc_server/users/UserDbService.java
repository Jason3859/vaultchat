package dev.jason.project.spring.vc_server.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDbService {

    @Autowired
    UserDbRepository repository;

    public void saveUser(DBUser DBUser) {
        repository.save(DBUser);
    }

    public DBUser getUserByUid(String uid) {
        return repository.findByUid(uid);
    }

    public List<DBUser> getUserByDisplayName(String displayName) {
        return repository.findByDisplayName(displayName);
    }

    public void updateUserFcmToken(String uid, String fcmToken) {
        DBUser existingUser = getUserByUid(uid);
        repository.save(new DBUser(existingUser.uid(), existingUser.displayName(), existingUser.profilePictureUrl(), fcmToken));
    }
}
