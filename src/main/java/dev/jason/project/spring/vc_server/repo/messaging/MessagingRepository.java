package dev.jason.project.spring.vc_server.repo.messaging;

import dev.jason.project.spring.vc_server.model.Device;
import dev.jason.project.spring.vc_server.model.Message;
import dev.jason.project.spring.vc_server.model.Result;
import dev.jason.project.spring.vc_server.model.User;

public interface MessagingRepository {

    Result sendMessage(Message message, Device device);
    Result sendUserStatusUpdate(Device device, String uid, User.Status status);
}
