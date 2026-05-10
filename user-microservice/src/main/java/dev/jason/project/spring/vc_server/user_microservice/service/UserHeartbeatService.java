package dev.jason.project.spring.vc_server.user_microservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.user_microservice.client.MessagingClient;

@Service
public class UserHeartbeatService extends UserService {

    @Autowired
    private MessagingClient messagingClient;

    @Scheduled(fixedRate = 30000, initialDelay = 2000) // Check every 30 seconds
    public void checkStaleHeartbeats() {
        long now = System.currentTimeMillis();
        repository.findAll().stream()
            .filter(user -> user.getStatus() != User.Status.Offline) // takes online users
            .filter(user -> (now - user.getLastHeartBeat()) >= 30000) // users of last heart beat more than 30 seconds
            .forEach(user -> {
                user.setStatus(User.Status.Offline);
                repository.save(user);
                messagingClient.notifyStatus(user.getUid(), User.Status.Offline);
            });
    }
}
