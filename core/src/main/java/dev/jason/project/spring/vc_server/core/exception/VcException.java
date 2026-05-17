package dev.jason.project.spring.vc_server.core.exception;

public class VcException extends RuntimeException {

	public static class UserException extends VcException {
		
		public static final class UserAlreadyExistsException extends UserException {}

		public static final class UserNotFoundException extends UserException {}
	}

	public static class DeviceException extends VcException {
		
		public static final class DeviceAlreadyExistsException extends DeviceException {}

		public static final class DeviceNotFoundException extends DeviceException {}
	}

	public static class SocialException extends VcException {
		
		public static final class BlockedByUserException extends SocialException {}

		public static final class NoUsersBlockedException extends SocialException {}

		public static final class SelfBlockException extends SocialException {}

		public static final class SelfUnblockException extends SocialException {}

		public static final class UserAlreadyBlockedException extends SocialException {}

		public static final class UserNotBlockedException extends SocialException {}
	}

	public static class MessagingException extends VcException {
		
		public static final class MessageTextBlankException extends MessagingException {}

		public static final class InternalError extends MessagingException {}
	}
}
