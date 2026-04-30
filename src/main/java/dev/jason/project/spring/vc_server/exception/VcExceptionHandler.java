package dev.jason.project.spring.vc_server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.jason.project.spring.vc_server.exception.VcException.*;
import dev.jason.project.spring.vc_server.model.Result;

@RestControllerAdvice
public class VcExceptionHandler {

	@ExceptionHandler(AdminSdkNotFoundException.class)
	public ResponseEntity<?> handleAdminSdkNotFoundException() {
		return new ResponseEntity<>(Result.Error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BlockedByUserException.class)
	public ResponseEntity<?> handleBlockedByUserException() {
		return new ResponseEntity<>(Result.BlockedByUser, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(DeviceAlreadyExistsException.class)
	public ResponseEntity<?> handleDeviceAlreadyExistsException() {
		return new ResponseEntity<>(Result.DeviceAlreadyExists, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(DeviceNotFoundException.class)
	public ResponseEntity<?> handleDeviceNotFoundException() {
		return new ResponseEntity<>(Result.DeviceNotFound, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NoUsersBlockedException.class)
	public ResponseEntity<?> handleNoUsersBlockedException() {
		return new ResponseEntity<>(Result.NoBlockedUsers, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(SelfBlockException.class)
	public ResponseEntity<?> handleSelfBlockException() {
		return new ResponseEntity<>(Result.SelfBlock, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(SelfUnblockException.class)
	public ResponseEntity<?> handleSelfUnblockException() {
		return new ResponseEntity<>(Result.SelfUnblock, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(UserAlreadyBlockedException.class)
	public ResponseEntity<?> handleUserAlreadyBlockedException() {
		return new ResponseEntity<>(Result.UserAlreadyBlocked, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<?> handleUserAlreadyExistsException() {
		return new ResponseEntity<>(Result.UserAlreadyExists, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UserNotBlockedException.class)
	public ResponseEntity<?> handleUserNotBlockedException() {
		return new ResponseEntity<>(Result.UserNotBlocked, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException() {
		return new ResponseEntity<>(Result.UserNotFound, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UsersAlreadyConnectedException.class)
	public ResponseEntity<?> handleUsersAlreadyConnectedException() {
		return new ResponseEntity<>(Result.UsersAlreadyConnected, HttpStatus.CONFLICT);
	}
}
