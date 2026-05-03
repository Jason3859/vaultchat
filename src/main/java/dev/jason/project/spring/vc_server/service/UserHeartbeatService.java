package dev.jason.project.spring.vc_server.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.model.User;

@Service
public class UserHeartbeatService extends UserService {

    @Scheduled(fixedRate = 30000, initialDelay = 2000) // Check every 30 seconds
    public void checkStaleHeartbeats() {
        long now = System.currentTimeMillis();
        repository.findAll().stream()
            .filter(user -> user.status() != User.Status.Offline) // takes online users
            .filter(user -> (now - user.lastHeartbeat()) >= 30000) // users of last heart beat more than 30 seconds
            .forEach(user -> {
            	updateUserStatusAndNotify(user.uid(), User.Status.Offline);
            });
    }
}
