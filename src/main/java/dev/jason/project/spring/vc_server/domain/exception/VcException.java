package dev.jason.project.spring.vc_server.domain.exception;

import java.io.Serial;

public sealed class VcException extends Exception
	permits AdminSdkNotFoundException,
	DeviceAlreadyExistsException,
	DeviceNotFoundException,
	NoUsersBlockedException,
	UserAlreadyBlockedException,
	UserAlreadyExistsException,
	UserNotBlockedException,
	UserNotFoundException {

    @Serial
    private static final long serialVersionUID = -8573188056532471500L;
}
