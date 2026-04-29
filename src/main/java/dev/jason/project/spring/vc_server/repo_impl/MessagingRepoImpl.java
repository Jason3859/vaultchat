package dev.jason.project.spring.vc_server.repo_impl;

import org.springframework.stereotype.Repository;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;

import dev.jason.project.spring.vc_server.model.Device;
import dev.jason.project.spring.vc_server.model.Message;
import dev.jason.project.spring.vc_server.model.Result;
import dev.jason.project.spring.vc_server.model.User;
import dev.jason.project.spring.vc_server.repo.messaging.MessagingRepository;

@Repository
public class MessagingRepoImpl implements MessagingRepository {
    @Override
    public Result sendMessage(Message message, Device device) {
        com.google.firebase.messaging.Message firebaseMessage = com.google.firebase.messaging.Message.builder()
            .setToken(device.token())
            .putData("type", "message")
            .putAllData(message.asMap())
            .build();

        try {
            FirebaseMessaging.getInstance().send(firebaseMessage);
            return Result.Success;
        } catch (FirebaseMessagingException e) {
            e.printStackTrace(System.err);
            return Result.Error;
        }
    }

    @Override
    public Result sendUserStatusUpdate(Device device, String uid, User.Status status) {
        com.google.firebase.messaging.Message firebaseMessage = com.google.firebase.messaging.Message.builder()
            .setToken(device.token())
            .putData("type", "status_update")
            .putData("uid", uid)
            .putData("status", status.toString())
            .build();

        try {
            FirebaseMessaging.getInstance().send(firebaseMessage);
            return Result.Success;
        } catch (FirebaseMessagingException e) {
            e.printStackTrace(System.err);
            return Result.Error;
        }
    }
}
