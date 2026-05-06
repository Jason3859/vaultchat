package dev.jason.project.spring.vc_server.device_microservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.jason.project.spring.vc_server.core.model.Result;

public class DeviceException extends RuntimeException {

	@ResponseStatus(HttpStatus.CONFLICT)
	public static final class DeviceAlreadyExistsException extends DeviceException {}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static final class DeviceNotFoundException extends DeviceException {}
	
	@RestControllerAdvice
	public static class Handler {
		
		@ExceptionHandler(DeviceAlreadyExistsException.class)
		public ResponseEntity<?> handleDeviceAlreadyExistsException() {
			return new ResponseEntity<>(Result.DeviceAlreadyExists, HttpStatus.CONFLICT);
		}
		
		@ExceptionHandler(DeviceNotFoundException.class)
		public ResponseEntity<?> handleDeviceNotFoundException() {
			return new ResponseEntity<>(Result.DeviceNotFound, HttpStatus.NOT_FOUND);
		}
	}
}
