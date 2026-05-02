package dev.jason.project.spring.vc_server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jason.project.spring.vc_server.dto.UserDto;
import dev.jason.project.spring.vc_server.service.UserService;

@RestController
@RequestMapping("/social")
public class UserSocialConnectionsController {
	
	@Autowired
	private UserService userService;

	@PatchMapping("/block")
	public ResponseEntity<?> blockUser(@RequestParam("from_uid") String fromUid, @RequestParam("other_uid") String otherUid) {
		userService.block(fromUid, otherUid);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PatchMapping("/unblock")
	public ResponseEntity<?> unblockUser(@RequestParam("from_uid") String fromUid, @RequestParam("other_uid") String otherUid) {
		userService.unblock(fromUid, otherUid);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PatchMapping("/connect")
	public ResponseEntity<?> connect(@RequestParam("from_uid") String fromUid, @RequestParam("other_uid") String otherUid) {
		userService.addConnection(fromUid, otherUid);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<UserDto>> search(@RequestParam("from_uid") String fromUid, @RequestParam("search_query") String searchQuery) {
		userService.getUserByUid(fromUid); // for verification that user exists
		
		List<UserDto> requiredUsers =  userService.getAllUsersByDisplayName(searchQuery).stream()
			.filter(user -> user.uid() != fromUid)
			.map(UserDto::fromDomainUser)
			.toList();
		
		return new ResponseEntity<>(requiredUsers, HttpStatus.OK);
	}
	
	@GetMapping("/get-connections")
	public ResponseEntity<List<UserDto>> connections(@RequestParam String uid) {
		List<UserDto> connections = userService.getUserByUid(uid).connections().stream()
			.map(userService::getUserByUid)
			.map(UserDto::fromDomainUser)
			.toList();
		
		return new ResponseEntity<>(connections, HttpStatus.OK);
	}
	
	@GetMapping("/get-blocked-users")
	public ResponseEntity<List<UserDto>> blockedUsers(@RequestParam String uid) {
		List<UserDto> blockedUsers = userService.getUserByUid(uid).blocklist().stream()
			.map(userService::getUserByUid)
			.map(UserDto::fromDomainUser)
			.toList();
		
		return new ResponseEntity<>(blockedUsers, HttpStatus.OK);
	}
}
