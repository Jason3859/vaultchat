package dev.jason.project.spring.vc_server.user_dms;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dms")
public record UserDms(@Id String uid, String[] dms) {}
