package dev.jason.project.spring.vc_server.test;

import dev.jason.project.spring.vc_server.data.dto.DeviceDto;
import dev.jason.project.spring.vc_server.data.dto.RegisterUserDto;
import dev.jason.project.spring.vc_server.domain.model.Device;
import dev.jason.project.spring.vc_server.domain.model.Message;
import dev.jason.project.spring.vc_server.domain.model.User;
import dev.jason.project.spring.vc_server.domain.model.Device.OS;
import dev.jason.project.spring.vc_server.domain.model.Device.Type;

import java.time.LocalDateTime;

public class TestConstants {
	public static final Device TEST_DEVICE_1 =
		new Device("test_device_1", Type.Mobile, OS.Android, "10", "token", LocalDateTime.now());

	public static final Device TEST_DEVICE_2 =
		new Device("test_device_2", Type.Mobile, OS.Android, "10", "token", LocalDateTime.now());

	public static final Message DEFAULT_MESSAGE =
		new Message("me", "to", "text", "timestamp");

	public static final User TEST_USER_1 = getTestUser1(TEST_DEVICE_1);

	public static final User TEST_USER_2 = getTestUser2(TEST_DEVICE_2);

	public static final User UNKNOWN_USER_1 =
		new RegisterUserDto(
			"unknown_user_1",
			"UNKNOWN_USER_1",
			"url",
			DeviceDto.fromDomain(TEST_DEVICE_1)
		).toDomainUser();

	public static final User UNKNOWN_USER_2 =
		new RegisterUserDto(
			"unknown_user_2",
			"UNKNOWN_USER_2",
			"url",
			DeviceDto.fromDomain(TEST_DEVICE_1)
		).toDomainUser();

	public static User getTestUser1(Device device) {
		return new RegisterUserDto(
			"test_user_1",
			"Test User 1",
			"url",
			DeviceDto.fromDomain(device)
		).toDomainUser();
	}

	public static User getTestUser2(Device device) {
		return new RegisterUserDto(
			"test_user_2",
			"Test User 2",
			"url",
			DeviceDto.fromDomain(device)
		).toDomainUser();
	}
}
