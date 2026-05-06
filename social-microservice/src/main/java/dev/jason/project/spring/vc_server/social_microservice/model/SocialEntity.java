package dev.jason.project.spring.vc_server.social_microservice.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.jason.project.spring.vc_server.core.model.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Document(collection = "social")
@AllArgsConstructor
@Getter
public class SocialEntity {
	
	@Id
	private final String userId;
	private final List<String> blocklist;
	private final List<String> connections;
	private final List<Message> queuedMessages;
}
