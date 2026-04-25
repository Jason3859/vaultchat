package dev.jason.project.spring.vc_server.domain.repo.messaging;

import dev.jason.project.spring.vc_server.domain.model.Device;
import dev.jason.project.spring.vc_server.domain.model.Message;
import dev.jason.project.spring.vc_server.domain.model.Result;
import dev.jason.project.spring.vc_server.domain.model.User;

public interface MessagingRepository {

    Result sendMessage(Message message, Device device);
    Result sendUserStatusUpdate(Device device, String uid, User.Status status);
}
