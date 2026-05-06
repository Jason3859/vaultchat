package dev.jason.project.spring.vc_server.user_microservice.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.core.model.User;

@Service
public class UserHeartbeatService extends UserService {

    @Scheduled(fixedRate = 30000, initialDelay = 2000) // Check every 30 seconds
    public void checkStaleHeartbeats() {
        long now = System.currentTimeMillis();
        repository.findAll().stream()
            .filter(user -> user.getStatus() != User.Status.Offline) // takes online users
            .filter(user -> (now - user.getLastHeartBeat()) >= 30000) // users of last heart beat more than 30 seconds
            .forEach(_ -> {
//            	updateUserStatusAndNotify(user.uid(), User.Status.Offline);

                // TODO: 05/05/2026 update status and notify
            });
    }
}
