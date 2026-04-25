package dev.jason.project.spring.vc_server.domain.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.model.User;
import dev.jason.project.spring.vc_server.domain.repo.db.user.UserEntity;

@Service
public class UserHeartbeatService extends UserService {
	
    public void updateHeartbeat(String uid) throws UserNotFoundException {
        UserEntity entity = getUserEntityOrThrow(uid);
        entity.setLastHeartbeat(System.currentTimeMillis());
        userRepository.save(entity);
        if (entity.status() == User.Status.Offline) {
            updateUserStatusAndNotify(uid, User.Status.Online);
        }
    }

    @Scheduled(fixedRate = 30000, initialDelay = 2000) // Check every 30 seconds
    public void checkStaleHeartbeats() {
        long now = System.currentTimeMillis();
        userRepository.findAll().stream()
            .filter(user -> user.status() != User.Status.Offline) // takes online users
            .filter(user -> (now - user.lastHeartbeat()) >= 30000) // users of last heart beat less than 30 seconds
            .forEach(user -> {
                try {
                    updateUserStatusAndNotify(user.uid(), User.Status.Offline);
                } catch (UserNotFoundException ignored) {
                }
            });
    }
}
