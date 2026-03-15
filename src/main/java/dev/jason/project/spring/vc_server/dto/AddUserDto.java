package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.service.VCUtilityService;
import dev.jason.project.spring.vc_server.users.UserDbEntity;

public record AddUserDto(String uid, String fcmToken) {

    public UserDbEntity toDbUser(String[] connections) {
        User user = VCUtilityService.getUserFromUid(uid);

        return new UserDbEntity(user.uid(), user.displayName(), user.profilePictureUrl(), fcmToken, connections);
    }
}
