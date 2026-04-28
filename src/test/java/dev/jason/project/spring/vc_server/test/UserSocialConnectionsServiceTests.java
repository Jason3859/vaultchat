package dev.jason.project.spring.vc_server.test;

import static dev.jason.project.spring.vc_server.test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.jason.project.spring.vc_server.domain.exception.BlockedByUserException;
import dev.jason.project.spring.vc_server.domain.exception.UserAlreadyBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.exception.UsersAlreadyConnectedException;
import dev.jason.project.spring.vc_server.domain.repo.db.user.UserEntity;
import dev.jason.project.spring.vc_server.domain.repo.db.user.UserRepository;
import dev.jason.project.spring.vc_server.domain.service.UserSocialConnectionsService;

@SpringBootTest
public class UserSocialConnectionsServiceTests {

	@Autowired
	private UserSocialConnectionsService service;
	
	@Autowired
	private UserRepository repository;
	
	@Nonnull
	private final List<UserEntity> testUsers = 
		Stream.of(TEST_USER_1, TEST_USER_2)
			.map(UserEntity::fromDomainUser).toList();
	
	@BeforeEach
	void setup() {
		repository.saveAll(testUsers);
	}
	
	@AfterEach
	void deleteTestUsers() {
		repository.deleteAll(testUsers);
	}
	
	@Test
	void connecting_two_existing_users_succeeds() {
		try {
			service.addConnection(TEST_USER_1, TEST_USER_2);
		} catch (UserNotFoundException | UsersAlreadyConnectedException | BlockedByUserException e) {
		}
		
		UserEntity entity1 = repository.findByUid(TEST_USER_1.uid());
		UserEntity entity2 = repository.findByUid(TEST_USER_2.uid());
		
		assertTrue(entity1.connections().contains(entity2.uid()));
		assertTrue(entity2.connections().contains(entity1.uid()));
	}
	
	@Test
	void connecting_an_existing_user_to_unknown_user_throws_exception() {
		assertThrows(UserNotFoundException.class, () -> {
			service.addConnection(TEST_USER_1, UNKNOWN_USER_1);
		});
	}
	
	@Test
	void connecting_two_unknown_users_throws_exception() {
		assertThrows(UserNotFoundException.class, () -> {
			service.addConnection(UNKNOWN_USER_1, UNKNOWN_USER_2);
		});
	}
	
	@Test
	void connecting_two_already_connected_user_throws_exception() {
		try {
			service.addConnection(TEST_USER_1, TEST_USER_2);
		} catch (UserNotFoundException | UsersAlreadyConnectedException | BlockedByUserException e) {
		}
		
		assertThrows(UsersAlreadyConnectedException.class, () -> {
			service.addConnection(TEST_USER_1, TEST_USER_2);
		});
	}
	
	@Test
	void connecting_two_users_where_one_blocked_other_throws_exception() {
		try {
			service.block(TEST_USER_1.uid(), TEST_USER_2.uid());
		} catch (UserNotFoundException | UserAlreadyBlockedException e) {
		}
		
		assertThrows(BlockedByUserException.class, () -> {
			service.addConnection(TEST_USER_1, TEST_USER_2);
		});
	}
	
	@Test
	void blocking_two_registered_users_succeeds() {
		String uid1 = TEST_USER_1.uid();
		String uid2 = TEST_USER_2.uid();
		
		try {
			service.block(uid1, uid2);
		} catch (UserNotFoundException | UserAlreadyBlockedException e) {
		}
		
		UserEntity entity = repository.findByUid(uid1);
		assertTrue(entity.blocklist().contains(uid2));
	}
	
	@Test
	void blocking_user_1_from_user_2_where_user_2_blocked_user_1_already_succeeds() {
		String uid1 = TEST_USER_1.uid();
		String uid2 = TEST_USER_2.uid();
		
		try {
			service.block(uid2, uid1);
		} catch (UserNotFoundException | UserAlreadyBlockedException e) {
		}
		
		UserEntity entity = repository.findByUid(uid1);
		assertTrue(entity.blocklist().contains(uid2));
	}
	
	@Test
	void reblocking_users_throws_exception() {
		String uid1 = TEST_USER_1.uid();
		String uid2 = TEST_USER_2.uid();
		
		try {
			service.block(uid1, uid2);
		} catch (UserNotFoundException | UserAlreadyBlockedException e) {
		}
		
		assertThrows(UserAlreadyBlockedException.class, () -> {
			service.block(uid1, uid2);
		});
	}
	
	@Test
	void blocking_two_unknown_users_throws_exception() {
		assertThrows(UserNotFoundException.class, () -> {
            service.block("unknown_1", "unknown_2");
        });
	}

	// TODO: more tests
}
