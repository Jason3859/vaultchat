package dev.jason.project.spring.vc_server.user_microservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.jason.project.spring.vc_server.core.model.Result;

public class UserException extends RuntimeException {
	
	public static final class AdminSdkNotFoundException extends UserException {}

	@ResponseStatus(HttpStatus.CONFLICT)
	public static final class UserAlreadyExistsException extends UserException {}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class UserNotFoundException extends UserException {}
	
	@RestControllerAdvice
	public static class Handler {
		
		@ExceptionHandler(UserAlreadyExistsException.class)
		public ResponseEntity<?> handleUserAlreadyExistsException() {
			return new ResponseEntity<>(Result.UserAlreadyExists, HttpStatus.CONFLICT);
		}

		@ExceptionHandler(UserNotFoundException.class)
		public ResponseEntity<?> handleUserNotFoundException() {
			return new ResponseEntity<>(Result.UserNotFound, HttpStatus.NOT_FOUND);
		}
	}
}
