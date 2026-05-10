package dev.jason.project.spring.vc_server.core;

public class Endpoints {
	
	public static final int DEVICE_MICROSERVICE_PORT = 9000;
	public static final int MESSAGING_MICROSERVICE_PORT = 9001;
	public static final int SOCIAL_MICROSERVICE_PORT = 9002;
	public static final int USER_MICROSERVICE_PORT = 9003;

	public static final String DEVICE_SERVICE_URL = "http://localhost:" + DEVICE_MICROSERVICE_PORT;
	public static final String MESSAGING_SERVICE_URL = "http://localhost:" + MESSAGING_MICROSERVICE_PORT;
	public static final String SOCIAL_SERVICE_URL = "http://localhost:" + SOCIAL_MICROSERVICE_PORT;
	public static final String USER_SERVICE_URL = "http://localhost:" + USER_MICROSERVICE_PORT;

	public static final String DEVICE = "/device";
	public static final String MESSAGING = "/messaging";
	public static final String SOCIAL = "/social";
	public static final String USER = "/user";

	public static final String ADD = "/add";
	public static final String ADD_MESSAGE_TO_QUEUE = "/add-message-to-queue";
	public static final String BLOCK = "/block";
	public static final String CONNECT = "/connect";
	public static final String DELETE = "/delete";
	public static final String GET_BLOCKED_USERS = "/get-blocked-users";
	public static final String GET_CONNECTIONS = "/get-connections";
	public static final String GET_DEVICES_BY_OWNER = "/get-devices-by-owner";
	public static final String GET_USER_BY_UID = "/get-user-by-uid";
	public static final String HEARTBEAT = "/heartbeat";
	public static final String MY_DEVICES = "/my-devices";
	public static final String NOTIFY_STATUS = "/notify-status";
	public static final String REGISTER = "/register";
	public static final String SEARCH = "/search";
	public static final String SEND = "/send";
	public static final String UNBLOCK = "/unblock";

	public static final String DEVICE_ADD = DEVICE + ADD;
	public static final String DEVICE_GET_DEVICES_BY_OWNER = DEVICE + GET_DEVICES_BY_OWNER;
	
	public static final String MESSAGING_NOTIFY_STATUS = MESSAGING + NOTIFY_STATUS;
	
	public static final String SOCIAL_ADD_MESSAGE_TO_QUEUE = SOCIAL + ADD_MESSAGE_TO_QUEUE;
	public static final String SOCIAL_CONNECT = SOCIAL + CONNECT;
	public static final String SOCIAL_GET_CONNECTIONS = SOCIAL + GET_CONNECTIONS;
	public static final String SOCIAL_REGISTER = SOCIAL + REGISTER;
	
	public static final String USER_GET_USER_BY_UID = USER + GET_USER_BY_UID;
}
