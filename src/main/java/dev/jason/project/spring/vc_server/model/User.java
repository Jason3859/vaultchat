package dev.jason.project.spring.vc_server.model;

import java.util.List;

public record User(
    String uid,
    String displayName,
    String profilePictureUrl,
    List<Device> devices,
    List<String> connections,
    List<String> blocklist,
    Status status,
    long lastHeartbeat
) {
    public enum Status {
        Online, Away, Offline
    }
    
    @Override
    public boolean equals(Object other) {
    	if (this == other) {
			return true;
		}
    	
    	if (!(other instanceof User)) {
			return false;
		}
    	
    	return this.uid().equals(((User) other).uid());
    }
}
