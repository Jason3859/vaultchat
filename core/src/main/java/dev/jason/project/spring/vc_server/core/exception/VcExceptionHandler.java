package dev.jason.project.spring.vc_server.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.jason.project.spring.vc_server.core.model.ErrorResponse;
import dev.jason.project.spring.vc_server.core.model.Result;

@RestControllerAdvice
public class VcExceptionHandler {

	@ExceptionHandler(VcException.UserException.UserAlreadyExistsException.class)
	public ResponseEntity<?> handleUserAlreadyExistsException() {
		return error(Result.UserAlreadyExists, HttpStatus.CONFLICT, "User already exists.");
	}

	@ExceptionHandler(VcException.UserException.UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException() {
		return error(Result.UserNotFound, HttpStatus.NOT_FOUND, "User was not found.");
	}

	@ExceptionHandler(VcException.DeviceException.DeviceAlreadyExistsException.class)
	public ResponseEntity<?> handleDeviceAlreadyExistsException() {
		return error(Result.DeviceAlreadyExists, HttpStatus.CONFLICT, "Device already exists.");
	}

	@ExceptionHandler(VcException.DeviceException.DeviceNotFoundException.class)
	public ResponseEntity<?> handleDeviceNotFoundException() {
		return error(Result.DeviceNotFound, HttpStatus.NOT_FOUND, "Device was not found.");
	}

	@ExceptionHandler(VcException.SocialException.BlockedByUserException.class)
	public ResponseEntity<?> handleSocialBlockedByUserException() {
		return error(Result.BlockedByUser, HttpStatus.FORBIDDEN, "Request is blocked by the user.");
	}

	@ExceptionHandler(VcException.SocialException.NoUsersBlockedException.class)
	public ResponseEntity<?> handleNoUsersBlockedException() {
		return error(Result.NoBlockedUsers, HttpStatus.NOT_FOUND, "No blocked users were found.");
	}

	@ExceptionHandler(VcException.SocialException.SelfBlockException.class)
	public ResponseEntity<?> handleSelfBlockException() {
		return error(Result.SelfBlock, HttpStatus.NOT_ACCEPTABLE, "User cannot block themselves.");
	}

	@ExceptionHandler(VcException.SocialException.SelfUnblockException.class)
	public ResponseEntity<?> handleSelfUnblockException() {
		return error(Result.SelfUnblock, HttpStatus.NOT_ACCEPTABLE, "User cannot unblock themselves.");
	}

	@ExceptionHandler(VcException.SocialException.UserAlreadyBlockedException.class)
	public ResponseEntity<?> handleUserAlreadyBlockedException() {
		return error(Result.UserAlreadyBlocked, HttpStatus.CONFLICT, "User is already blocked.");
	}

	@ExceptionHandler(VcException.SocialException.UserNotBlockedException.class)
	public ResponseEntity<?> handleUserNotBlockedException() {
		return error(Result.UserNotBlocked, HttpStatus.NOT_FOUND, "User is not blocked.");
	}

	@ExceptionHandler(VcException.MessagingException.MessageTextBlankException.class)
	public ResponseEntity<?> handleMessageTextBlankException() {
		return error(Result.MessageTextBlank, HttpStatus.NOT_ACCEPTABLE, "Message text cannot be blank.");
	}

	@ExceptionHandler(VcException.MessagingException.InternalError.class)
	public ResponseEntity<?> handleMessagingException() {
		return error(Result.Error, HttpStatus.INTERNAL_SERVER_ERROR, "Internal messaging error.");
	}

	private ResponseEntity<ErrorResponse> error(Result result, HttpStatus status, String message) {
		return new ResponseEntity<>(
			new ErrorResponse(result, status.value(), status.getReasonPhrase(), message),
			status
		);
	}
}
