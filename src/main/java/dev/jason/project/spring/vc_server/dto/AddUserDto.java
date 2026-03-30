package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.service.VCUtilityService;
import dev.jason.project.spring.vc_server.users.UserEntity;

public record AddUserDto(String uid, String fcmToken) {

    public UserEntity toDbUser(boolean isTestUser) {
        UserEntity entity;

        if (!isTestUser) {
            User user = VCUtilityService.getUserFromUid(uid);
            entity = new UserEntity(
                user.uid(), user.displayName(), user.profilePictureUrl(), fcmToken, null, null
            );
        } else {
            entity = new UserEntity(
                uid, String.format("test-user@%s", uid), null, null, null, null
            );
        }

        return entity;
    }
}
