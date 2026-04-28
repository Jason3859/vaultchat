package dev.jason.project.spring.vc_server.test;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.jason.project.spring.vc_server.domain.exception.DeviceAlreadyExistsException;
import dev.jason.project.spring.vc_server.domain.exception.DeviceNotFoundException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.model.Device;
import dev.jason.project.spring.vc_server.domain.repo.db.user.UserEntity;
import dev.jason.project.spring.vc_server.domain.repo.db.user.UserRepository;
import dev.jason.project.spring.vc_server.domain.service.UserDeviceService;

@SpringBootTest
public class UserDeviceServiceTests {

    @Autowired
    private UserDeviceService service;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setup() {
        repository.save(UserEntity.fromDomainUser(TestConstants.TEST_USER_1));
        repository.save(UserEntity.fromDomainUser(TestConstants.TEST_USER_2));
    }

	@AfterEach
    void delete_test_users() {
        repository.deleteAll(
            Stream.of(TestConstants.TEST_USER_2, TestConstants.TEST_USER_2)
                .map(UserEntity::fromDomainUser).toList()
        );
    }

    @Test
    void adding_device_for_unknown_user_throws_exception() {
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            service.addDevice("unknown", TestConstants.TEST_DEVICE_1);
        });
    }

    @Test
    void adding_device_that_is_already_registered_throws_exception() {
        Assertions.assertThrows(DeviceAlreadyExistsException.class, () -> {
            service.addDevice(TestConstants.TEST_USER_1.uid(), TestConstants.TEST_DEVICE_1);
        });
    }

    @Test
    void adding_device_for_known_user_succeeds() {
        String uid = TestConstants.TEST_USER_1.uid();

        try {
            service.addDevice(uid, TestConstants.TEST_DEVICE_2);
        } catch (UserNotFoundException | DeviceAlreadyExistsException ignored) {
        }

        UserEntity entity = repository.findByUid(uid);
        Assertions.assertTrue(entity.devices().contains(TestConstants.TEST_DEVICE_2));
    }
    
    @Test
    void deleting_registered_device_for_known_user_succeeds() {
    	String uid = TestConstants.TEST_USER_1.uid();
    	Device device = TestConstants.TEST_DEVICE_1;
    	
    	try {
    		service.deleteDevice(uid, device);
    	} catch (UserNotFoundException | DeviceNotFoundException e) {
		}
    	
    	UserEntity entity = repository.findByUid(uid);
    	Assertions.assertFalse(entity.devices().contains(device));
    }
    
    @Test
    void deleting_unregistered_device_for_known_user_throws_exception() {
    	String uid = TestConstants.TEST_USER_1.uid();
    	Device device = TestConstants.TEST_DEVICE_2;
    	
    	Assertions.assertThrows(DeviceNotFoundException.class, () -> {
    		service.deleteDevice(uid, device);
    	});
    }
    
    @Test
    void deleting_device_for_unknown_user_throws_exception() {
    	Assertions.assertThrows(UserNotFoundException.class, () -> {
    		service.deleteDevice("unknown", TestConstants.TEST_DEVICE_1);
    	});
    }
    
    @Test
    void updating_token_of_unregistered_device_throws_exception() {
    	String uid = TestConstants.TEST_USER_1.uid();
    	Device device = TestConstants.TEST_DEVICE_2;
    	String newToken = "new-token";
    	
    	Assertions.assertThrows(DeviceNotFoundException.class, () -> {
    		service.updateToken(uid, newToken, device);
    	});
    }

	@Test
    void updating_token_of_registered_device_succeeds() {
        String uid = TestConstants.TEST_USER_1.uid();
        Device device = TestConstants.TEST_DEVICE_1;
        String newToken = "new-token";

        try {
            service.updateToken(uid, newToken, device);
        } catch (Exception ignored) {
        }

        UserEntity entity = repository.findByUid(uid);
        entity.devices().forEach(d -> {
            if (d.name().equals(device.name())) {
                Assertions.assertEquals(newToken, d.token());
            }
        });
    }

    @Test
    void updating_token_of_device_of_unregistered_user_throws_exception() {
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            service.updateToken("unknown", "new-token", TestConstants.TEST_DEVICE_1);
        });
    }
}
