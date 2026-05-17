package dev.jason.project.spring.vc_server.user_microservice.service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserAlreadyExistsException;
import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserNotFoundException;
import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.model.User.Status;
import dev.jason.project.spring.vc_server.user_microservice.client.DeviceClient;
import dev.jason.project.spring.vc_server.user_microservice.client.MessagingClient;
import dev.jason.project.spring.vc_server.user_microservice.client.SocialClient;
import dev.jason.project.spring.vc_server.user_microservice.model.UserEntity;
import dev.jason.project.spring.vc_server.user_microservice.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	protected UserRepository repository;
	
	@Autowired
	protected SocialClient socialClient;
	
	@Autowired
	protected DeviceClient deviceClient;
	
	@Autowired
	protected MessagingClient messagingClient;

	public User getUserById(String id) {
	 	Optional<UserEntity> entity = repository.findById(id);

	 	if (entity.isPresent()) {
			return entity.get().asUser();
		}

		throw new UserNotFoundException();
	}
	
	public Map.Entry<User, Device> addUser(User user, Device device) {
		Optional<UserEntity> entity = repository.findById(user.uid());
		
		if (entity.isPresent()) {
			throw new UserAlreadyExistsException();
		}
		
		repository.save(UserEntity.asEntity(user));
		socialClient.register(user.uid());
		Device d = deviceClient.register(DeviceDto.asDto(device)).toDevice(null);
		
		return new AbstractMap.SimpleEntry<>(user, d);
	}

	public void deleteUser(String uid) {
		Optional<UserEntity> entity = repository.findById(uid);

		if (entity.isPresent()) {
			repository.delete(entity.get());
			return;
		}

		throw new UserNotFoundException();
	}

	public void updateHeartBeat(String uid) {
		Optional<UserEntity> entity = repository.findById(uid);
		
		if (entity.isEmpty()) {
			throw new UserNotFoundException();
		}

		UserEntity e = entity.get();
		
		e.setLastHeartBeat(System.currentTimeMillis());
		e.setStatus(Status.Online);
		
		repository.save(e);
		
		messagingClient.notifyStatus(uid, Status.Online);
	}

    public List<User> getAllUsersByDisplayName(String query) {
		return repository.findByDisplayNameContainingIgnoreCase(query).stream()
			.map(UserEntity::asUser)
			.toList();
    }
}
