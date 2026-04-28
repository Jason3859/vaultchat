package dev.jason.project.spring.vc_server.domain.model;

public enum Result {
    BlockedByUser, DeviceNotFound,
    DeviceWithThisFcmTokenAlreadyExists, Error,
    MessageTextBlank, NoBlockedUsers, NoUsersFound,
    SelfBlock, SelfUnblock, Success, UserAlreadyBlocked,
    UserAlreadyExists, UserNotBlocked, UserNotFound, UsersAlreadyConnected
}