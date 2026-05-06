package dev.jason.project.spring.vc_server.social_microservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.jason.project.spring.vc_server.core.model.Result;

public class SocialException extends RuntimeException {

	@ResponseStatus(HttpStatus.FORBIDDEN)
	public static final class BlockedByUserException extends SocialException {}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class NoUsersBlockedException extends SocialException {}
	
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public static final class SelfBlockException extends SocialException {}
	
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public static final class SelfUnblockException extends SocialException {}

	@ResponseStatus(HttpStatus.CONFLICT)
	public static final class UserAlreadyBlockedException extends SocialException {}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class UserNotBlockedException extends SocialException {}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class UserNotFoundException extends SocialException {}
	
	@RestControllerAdvice
	public static class Handler {
		
		@ExceptionHandler(BlockedByUserException.class)
		public ResponseEntity<?> handleBlockedByUserException() {
			return new ResponseEntity<>(Result.BlockedByUser, HttpStatus.FORBIDDEN);
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
		
		@ExceptionHandler(UserNotBlockedException.class)
		public ResponseEntity<?> handleUserNotBlockedException() {
			return new ResponseEntity<>(Result.UserNotBlocked, HttpStatus.NOT_FOUND);
		}

		@ExceptionHandler(UserNotFoundException.class)
		public ResponseEntity<?> handleUserNotFoundException() {
			return new ResponseEntity<>(Result.UserNotFound, HttpStatus.NOT_FOUND);
		}
	}
}
