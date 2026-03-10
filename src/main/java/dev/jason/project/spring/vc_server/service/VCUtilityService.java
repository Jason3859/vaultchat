package dev.jason.project.spring.vc_server.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.users.UserDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VCUtilityService {

    @Autowired
    private static UserDbService userDbService;

    public static User getUserFromUid(String uid) {
        try {
            UserRecord decoded = FirebaseAuth.getInstance().getUser(uid);
            return new User(decoded.getUid(), decoded.getDisplayName(), decoded.getPhotoUrl());
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUserFcmTokenFromUid(String uid) {
        return userDbService.getUserByUid(uid).fcmToken();
    }
}
