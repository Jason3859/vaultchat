package dev.jason.project.spring.vc_server.core.model;

public enum Result {
    BlockedByUser, 
    DeviceNotFound, 
    DeviceAlreadyExists, 
    Error,
    MessageTextBlank, 
    NoBlockedUsers, 
    NoUsersFound,
    SelfBlock, 
    SelfUnblock, 
    Success, 
    UserAlreadyBlocked,
    UserAlreadyExists, 
    UserNotBlocked, 
    UserNotFound, 
    UsersAlreadyConnected, 
}