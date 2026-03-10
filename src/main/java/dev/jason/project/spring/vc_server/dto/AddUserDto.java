package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.service.VCUtilityService;
import dev.jason.project.spring.vc_server.users.DBUser;

public record AddUserDto(String uid, String fcmToken) {

    public DBUser toDbUser() {
        User user = VCUtilityService.getUserFromUid(uid);

        return new DBUser(user.uid(), user.displayName(), user.profilePictureUrl(), fcmToken);
    }
}
