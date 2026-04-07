package dev.jason.project.spring.vc_server.dto;

public record ResultDto(Result result, Object data) {

    public ResultDto(Result result) {
        this(result, null);
    }

    public enum Result {
        Success, InternalServerError, BlockedByUser, AlreadyBlocked,
        SelfBlock, UserAlreadyExists, SelfUnblock, MessageTextBlank,
        UserNotFound, NoBlockedUsers, NoUsersFound, UserNotBlocked, NotOnline
    }
}
