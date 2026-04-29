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

import dev.jason.project.spring.vc_server.model.User;
import dev.jason.project.spring.vc_server.service.UserService;

@RestController
@RequestMapping("/social")
public class UserSocialConnectionsController {
	
	@Autowired
	private UserService userService;

	@PatchMapping("/block")
	public ResponseEntity<?> blockUser(@RequestParam String fromUid, @RequestParam String otherUid) {
		userService.block(fromUid, otherUid);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PatchMapping("/unblock")
	public ResponseEntity<?> unblockUser(@RequestParam String fromUid, @RequestParam String otherUid) {
		userService.unblock(fromUid, otherUid);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PatchMapping("/connect")
	public ResponseEntity<?> connect(@RequestParam String fromUid, @RequestParam String otherUid) {
		userService.addConnection(fromUid, otherUid);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<User>> search(@RequestParam String fromUid, @RequestParam String searchQuery) {
		userService.getUserByUid(fromUid); // for verification that user exists
		
		List<User> requiredUsers =  userService.getAllUsersByDisplayName(searchQuery).stream()
			.filter(user -> user.uid() != fromUid)
			.toList();
		
		return new ResponseEntity<>(requiredUsers, HttpStatus.OK);
	}
}
