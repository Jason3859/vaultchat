package dev.jason.project.spring.vc_server.user_microservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.user_microservice.exception.UserException.UserAlreadyExistsException;
import dev.jason.project.spring.vc_server.user_microservice.exception.UserException.UserNotFoundException;
import dev.jason.project.spring.vc_server.user_microservice.model.UserEntity;
import dev.jason.project.spring.vc_server.user_microservice.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	protected UserRepository repository;

	public User getUserById(String id) {
	 	Optional<UserEntity> entity = repository.findById(id);

	 	if (entity.isPresent()) {
			return entity.get().asUser();
		}

		throw new UserNotFoundException();
	}
	
	public void addUser(User user) {
		Optional<UserEntity> entity = repository.findById(user.uid());
		
		if (entity.isEmpty()) {
			repository.save(UserEntity.asEntity(user));
			return;
		}
		
		throw new UserAlreadyExistsException();
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

		if (entity.isPresent()) {
			entity.get().setLastHeartBeat(System.currentTimeMillis());
			repository.save(entity.get());
			return;
		}

		throw new UserNotFoundException();
	}

    public List<User> getAllUsersByDisplayName(String query) {
		return repository.findByDisplayNameContainingIgnoreCase(query).stream()
			.map(UserEntity::asUser)
			.toList();
    }
}
