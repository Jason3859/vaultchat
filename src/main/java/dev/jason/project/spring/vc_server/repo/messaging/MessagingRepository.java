package dev.jason.project.spring.vc_server.repo.messaging;

import dev.jason.project.spring.vc_server.model.Device;
import dev.jason.project.spring.vc_server.model.Message;
import dev.jason.project.spring.vc_server.model.User;

public interface MessagingRepository {

    void sendMessage(Message message, Device device, boolean forDeviceCheck);
    void sendUserStatusUpdate(Device device, String uid, User.Status status);
}
