package dev.jason.project.spring.vc_server.user_dms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserDmsService {
    @Autowired
    UserDmsRepository repository;

    public void addUserDm(String userUid, String dmName) {
        UserDms existing = repository.findByUid(userUid);

        if (existing == null) {
            UserDms dm = new UserDms(userUid, new String[] {dmName});
            repository.save(dm);
        } else {
            String[] existingDms = existing.dms();
            List<String> newDmsList = new ArrayList<>(Arrays.asList(existingDms));
            newDmsList.add(dmName);
            String[] newDms = newDmsList.toArray(new String[0]);
            repository.save(new UserDms(userUid, newDms));
        }
    }
}
