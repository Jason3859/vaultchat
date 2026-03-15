package dev.jason.project.spring.vc_server.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import dev.jason.project.spring.vc_server.domain.User;

public class VCUtilityService {

    public static User getUserFromUid(String uid) {
        try {
            UserRecord decoded = FirebaseAuth.getInstance().getUser(uid);
            return new User(decoded.getUid(), decoded.getDisplayName(), decoded.getPhotoUrl());
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return null;
        }
    }
}
