package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.domain.Logger;
import dev.jason.project.spring.vc_server.domain.exception.NoUsersBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.exception.VcException;

public record ResultDto(Result result, Object data) {

    public ResultDto(Result result) {
        this(result, null);
    }

    public enum Result {
        Success, InternalServerError, BlockedByUser, AlreadyBlocked,
        SelfBlock, UserAlreadyExists, SelfUnblock, MessageTextBlank,
        UserNotFound, NoBlockedUsers, NoUsersFound, UserNotBlocked, NotOnline
    }

    public static ResultDto fromVcException(VcException exception) {
        return switch (exception) {
            case UserNotFoundException ignored -> new ResultDto(ResultDto.Result.UserNotFound);
            case UserNotBlockedException ignored -> new ResultDto(ResultDto.Result.UserNotBlocked);
            case NoUsersBlockedException ignored -> new ResultDto(ResultDto.Result.UserNotBlocked);
            default -> {
                Logger.write(exception);
                yield new ResultDto(ResultDto.Result.InternalServerError);
            }
        };
    }
}
