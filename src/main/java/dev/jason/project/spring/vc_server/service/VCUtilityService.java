package dev.jason.project.spring.vc_server.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import dev.jason.project.spring.vc_server.domain.Logger;
import dev.jason.project.spring.vc_server.domain.User;

public class VCUtilityService {

    public static User getUserFromUid(String uid) {
        try {
            UserRecord decoded = FirebaseAuth.getInstance().getUser(uid);
            return new User(decoded.getUid(), decoded.getDisplayName(), decoded.getPhotoUrl(), null, null);
        } catch (FirebaseAuthException e) {
            Logger.write(e);
            return new User(uid, uid, null, null, null);
        }
    }
}
