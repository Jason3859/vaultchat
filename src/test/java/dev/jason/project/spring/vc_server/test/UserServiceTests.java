package dev.jason.project.spring.vc_server.test;

import dev.jason.project.spring.vc_server.domain.exception.UserAlreadyExistsException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.model.User;
import dev.jason.project.spring.vc_server.domain.repo.db.user.UserEntity;
import dev.jason.project.spring.vc_server.domain.repo.db.user.UserRepository;
import dev.jason.project.spring.vc_server.domain.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class UserServiceTests {
	
	@Autowired
	@Qualifier("userService")
	private UserService service;
	
	@Autowired
	private UserRepository repository;
	
	@BeforeEach
	void setup() {
		repository.save(UserEntity.fromDomainUser(TestConstants.TEST_USER_1));
		repository.save(UserEntity.fromDomainUser(TestConstants.TEST_USER_2));
	}
	
	@SuppressWarnings("null")
	@AfterEach
	void delete_test_users() {
		repository.deleteAll(
			Stream.of(TestConstants.TEST_USER_2, TestConstants.TEST_USER_2)
				.map(UserEntity::fromDomainUser).toList()
		);
	}
	
	@Test
	@SuppressWarnings("all") // for intellij idea complaining about lambda
	void adding_existing_user_with_same_device_throws_exception() {
		Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            service.saveUserOrThrow(TestConstants.TEST_USER_1);
        });
	}

	@Test
	void adding_existing_user_with_different_device_does_not_throw_exception() {
		User testUser2 = TestConstants.getTestUser2(TestConstants.TEST_DEVICE_2);

		try {
			service.saveUserOrThrow(testUser2);
		} catch (UserAlreadyExistsException ignored) {
        }

		List<UserEntity> existingUsers = repository.findAll();
		Assertions.assertTrue(existingUsers.contains(UserEntity.fromDomainUser(testUser2)));
    }

	@Test
	@SuppressWarnings("all") // for intellij idea complaining about lambda
	void adding_a_message_to_queue_of_unknown_user_throws_exception() {
		Assertions.assertThrows(UserNotFoundException.class, () -> {
			service.addMessageToQueue("unknown", TestConstants.DEFAULT_MESSAGE);
		});
	}

	@Test
	void adding_a_message_to_queue_of_known_user_succeeds() {
		try {
			service.addMessageToQueue(TestConstants.TEST_USER_1.uid(), TestConstants.DEFAULT_MESSAGE);
		} catch (UserNotFoundException ignored) {
        }

		UserEntity entity = repository.findByUid(TestConstants.TEST_USER_1.uid());
		Assertions.assertTrue(entity.queuedMessages().contains(TestConstants.DEFAULT_MESSAGE));
    }
}
