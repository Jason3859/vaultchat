package dev.jason.project.spring.vc_server.dto;

import javax.annotation.Nullable;

public record ResultDto(Result result, @Nullable String message) {

    public enum Result {
        Success, InternalError, UnknownError, ClientSideError
    }
}
