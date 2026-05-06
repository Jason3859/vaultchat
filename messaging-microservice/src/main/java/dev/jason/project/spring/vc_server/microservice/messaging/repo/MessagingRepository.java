package dev.jason.project.spring.vc_server.microservice.messaging.repo;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.model.User;

public interface MessagingRepository {

    void sendMessage(Message message, Device device, boolean forDeviceCheck);
    void sendUserStatusUpdate(Device device, String uid, User.Status status);
}
