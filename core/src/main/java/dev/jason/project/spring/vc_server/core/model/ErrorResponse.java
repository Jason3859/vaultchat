package dev.jason.project.spring.vc_server.core.model;

public record ErrorResponse(Result result, int status, String error, String message) {}
