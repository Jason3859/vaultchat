package dev.jason.project.spring.vc_server.user_microservice.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jason.project.spring.vc_server.core.dto.UserDto;
import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.user_microservice.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserDto userDto) {
		Map.Entry<User, Device> response =
			userService.addUser(userDto.asUser(), userDto.device().toDevice(userDto.uid(), LocalDateTime.now()));
		
		UserDto dto = UserDto.fromUserAndDevice(response.getKey(), response.getValue());
		
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUser(@RequestParam String uid) {
		userService.deleteUser(uid);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PatchMapping("/heartbeat")
	public ResponseEntity<?> heartbeat(@RequestParam String uid) {
		userService.updateHeartBeat(uid);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/get-user-by-id")
	public UserDto getUserById(@RequestParam String id) {
		User user = userService.getUserById(id);
		return UserDto.fromUser(user);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<UserDto>> search(@RequestParam("from_uid") String fromUid, @RequestParam("search_query") String searchQuery) {
		userService.getUserById(fromUid); // for verification that user exists
		
		List<UserDto> requiredUsers = userService.getAllUsersByDisplayName(searchQuery).stream()
			.map(UserDto::fromUser)
			.filter(user -> !Objects.equals(user.uid(), fromUid))
			.toList();
		
		return new ResponseEntity<>(requiredUsers, HttpStatus.OK);
	}
}
