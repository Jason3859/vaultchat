package dev.jason.project.spring.vc_server.domain;

public record User(String uid, String displayName, String profilePictureUrl, String[] connections, String[] blocklist) {}
