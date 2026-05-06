package dev.jason.project.spring.vc_server.microservice.messaging.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.jason.project.spring.vc_server.core.model.Result;

public class MessagingException extends RuntimeException {
	
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public static final class MessageTextBlankException extends MessagingException {}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public static final class InternalError extends MessagingException {}
	
	@RestControllerAdvice
	public static class Handler {
		
		@ExceptionHandler(MessageTextBlankException.class) 
		public ResponseEntity<?> handleMessageTextBlankException() {
			return new ResponseEntity<>(Result.MessageTextBlank, HttpStatus.NOT_ACCEPTABLE);
		}
		
		@ExceptionHandler(InternalError.class)
		public ResponseEntity<?> handleMessagingException() {
			return new ResponseEntity<>(Result.Error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
