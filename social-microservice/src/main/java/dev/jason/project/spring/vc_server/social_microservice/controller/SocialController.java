package dev.jason.project.spring.vc_server.social_microservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jason.project.spring.vc_server.core.Endpoints;
import dev.jason.project.spring.vc_server.core.dto.UserDto;
import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.social_microservice.client.UserClient;
import dev.jason.project.spring.vc_server.social_microservice.service.SocialService;

@RestController
@RequestMapping(Endpoints.SOCIAL)
public class SocialController {
	
	@Autowired
	private SocialService socialService;

	@Autowired
	private UserClient userClient;
	
	@PostMapping(Endpoints.REGISTER)
	public void register(@RequestParam String uid) {
		socialService.registerNewUser(uid);
	}

	@PatchMapping(Endpoints.BLOCK)
	public ResponseEntity<?> blockUser(@RequestParam("from_uid") String fromUid, @RequestParam("other_uid") String otherUid) {
		socialService.block(fromUid, otherUid);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PatchMapping(Endpoints.UNBLOCK)
	public ResponseEntity<?> unblockUser(@RequestParam("from_uid") String fromUid, @RequestParam("other_uid") String otherUid) {
		socialService.unblock(fromUid, otherUid);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PatchMapping(Endpoints.CONNECT)
	public ResponseEntity<?> connect(@RequestParam("from_uid") String fromUid, @RequestParam("other_uid") String otherUid) {
		socialService.addConnection(fromUid, otherUid);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@PatchMapping(Endpoints.ADD_MESSAGE_TO_QUEUE)
	public void addMessageToQueue(@RequestParam String uid, @RequestBody Message message) {
		socialService.addMessageToQueue(uid, message);
	}

	@GetMapping(Endpoints.GET_CONNECTIONS)
	public ResponseEntity<List<UserDto>> connections(@RequestParam String uid) {
		List<UserDto> connections = socialService.getSocialEntityByUid(uid).getConnections().stream()
			.map(userClient::getUserById)
			.toList();
		
		return new ResponseEntity<>(connections, HttpStatus.OK);
	}
	
	@GetMapping(Endpoints.GET_BLOCKED_USERS)
	public ResponseEntity<List<UserDto>> blockedUsers(@RequestParam String uid) {
		List<UserDto> blockedUsers = socialService.getSocialEntityByUid(uid).getBlocklist().stream()
			.map(userClient::getUserById)
			.toList();
		
		return new ResponseEntity<>(blockedUsers, HttpStatus.OK);
	}
}
