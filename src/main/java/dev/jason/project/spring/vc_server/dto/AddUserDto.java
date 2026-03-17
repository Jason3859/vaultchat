package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.service.VCUtilityService;
import dev.jason.project.spring.vc_server.users.UserDbEntity;

public record AddUserDto(String uid, String fcmToken) {

    public UserDbEntity toDbUser(boolean isTestUser) {
        UserDbEntity entity;

        if (!isTestUser) {
            User user = VCUtilityService.getUserFromUid(uid);
            entity = new UserDbEntity(user.uid(), user.displayName(), user.profilePictureUrl(), fcmToken, null, null);
        } else {
            entity = new UserDbEntity(
                uid, String.format("test-user@%s", uid), null, null, null, null
            );
        }

        return entity;
    }
}
