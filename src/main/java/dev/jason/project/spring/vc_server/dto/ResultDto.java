package dev.jason.project.spring.vc_server.dto;

public record ResultDto(Result result) {

    public enum Result {
        Success, InternalServerError, BlockedByUser, AlreadyBlocked,
        SelfBlock, UserAlreadyExists, SelfUnblock, MessageTextBlank,
        UserNotFound, UserDoesntExist, UserNotBlocked
    }
}
