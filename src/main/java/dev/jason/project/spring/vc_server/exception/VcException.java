package dev.jason.project.spring.vc_server.exception;

import java.io.Serial;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public sealed class VcException extends RuntimeException
	permits VcException.AdminSdkNotFoundException,
	VcException.BlockedByUserException,
    VcException.DeviceAlreadyExistsException,
    VcException.DeviceNotFoundException,
    VcException.NoUsersBlockedException,
    VcException.UserAlreadyBlockedException,
    VcException.UserAlreadyExistsException,
    VcException.UserNotBlockedException,
    VcException.UserNotFoundException,
    VcException.UsersAlreadyConnectedException {

    @Serial
    private static final long serialVersionUID = -8573188056532471500L;

	public static final class AdminSdkNotFoundException extends VcException {
		
		@Serial
		private static final long serialVersionUID = 5210048833845738907L;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	public static final class BlockedByUserException extends VcException {
		
		@Serial
		private static final long serialVersionUID = 7533602235358945335L;

	}

	@ResponseStatus(HttpStatus.CONFLICT)
	public static final class DeviceAlreadyExistsException extends VcException {

		@Serial
		private static final long serialVersionUID = 4191882644552066151L;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class DeviceNotFoundException extends VcException {

		@Serial
		private static final long serialVersionUID = 293872380193L;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class NoUsersBlockedException extends VcException {

		@Serial
		private static final long serialVersionUID = 1L;
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	public static final class UserAlreadyBlockedException extends VcException {

		@Serial
		private static final long serialVersionUID = -4885639557194297990L;
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	public static final class UserAlreadyExistsException extends VcException {

		@Serial
		private static final long serialVersionUID = 7979903072023424491L;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class UserNotBlockedException extends VcException {

		@Serial
		private static final long serialVersionUID = 3994829004714402747L;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class UserNotFoundException extends VcException {

		@Serial
		private static final long serialVersionUID = -2127564165030933657L;
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	public static final class UsersAlreadyConnectedException extends VcException {

		@Serial
		private static final long serialVersionUID = -4359781902061777816L;

	}
}
