package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.domain.exception.*;

public record ResultDto(Result result, Object data) {

    public ResultDto(Result result) {
        this(result, null);
    }

    public enum Result {
        AlreadyBlocked, BlockedByUser, DeviceNotFound,
        DeviceWithThisFcmTokenAlreadyExists, InternalServerError,
        MessageTextBlank, NoBlockedUsers, NoUsersFound,
        SelfBlock, SelfUnblock, Success, UserAlreadyExists, UserNotBlocked, UserNotFound
    }

    public static ResultDto fromVcException(VcException exception) {
        return switch (exception) {
            case AdminSdkNotFoundException ignored -> throw new RuntimeException("this method should not be called in case of AdminSdkNotFoundException");
            case DeviceAlreadyExistsException ignored -> new ResultDto(Result.DeviceWithThisFcmTokenAlreadyExists);
            case DeviceNotFoundException ignored -> new ResultDto(Result.DeviceNotFound);
            case NoUsersBlockedException ignored -> new ResultDto(Result.UserNotBlocked);
            case UserAlreadyBlockedException ignored -> new ResultDto(Result.AlreadyBlocked);
            case UserAlreadyExistsException ignored -> new ResultDto(Result.UserAlreadyExists);
            case UserNotBlockedException ignored -> new ResultDto(Result.UserNotBlocked);
            case UserNotFoundException ignored -> new ResultDto(Result.UserNotFound);
            default -> throw new IllegalStateException();
        };
    }
}
