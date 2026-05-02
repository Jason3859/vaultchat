package dev.jason.project.spring.vc_server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class VcException extends RuntimeException {

	public static final class AdminSdkNotFoundException extends VcException {}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	public static final class BlockedByUserException extends VcException {}

	@ResponseStatus(HttpStatus.CONFLICT)
	public static final class DeviceAlreadyExistsException extends VcException {}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class DeviceNotFoundException extends VcException {}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class NoUsersBlockedException extends VcException {}
	
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public static final class SelfBlockException extends VcException {}
	
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public static final class SelfUnblockException extends VcException {}

	@ResponseStatus(HttpStatus.CONFLICT)
	public static final class UserAlreadyBlockedException extends VcException {}

	@ResponseStatus(HttpStatus.CONFLICT)
	public static final class UserAlreadyExistsException extends VcException {}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class UserNotBlockedException extends VcException {}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class UserNotFoundException extends VcException {}
	
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public static final class MessageTextBlankException extends VcException {}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public static final class MessagingException extends VcException {}
}
