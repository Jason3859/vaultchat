package dev.jason.project.spring.vc_server.users;

import dev.jason.project.spring.vc_server.domain.Device;
import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.domain.UserStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

@Document(collection = "users")
public final class UserEntity {
    @Id
    private final String uid;
    private final String displayName;
    private final String profilePictureUrl;
    private final List<Device> devices;
    private final List<String> connections;
    private final List<String> blocklist;
    private UserStatus status;
    private long lastHeartbeat;

    public UserEntity(
        String uid,
        String displayName,
        String profilePictureUrl,
        List<Device> devices,
        List<String> connections,
        List<String> blocklist,
        UserStatus status,
        long lastHeartbeat
    ) {
        this.uid = uid;
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
        this.devices = devices;
        this.connections = connections;
        this.blocklist = blocklist;
        this.status = status;
        this.lastHeartbeat = lastHeartbeat;
    }
    
    @Nonnull
    public static UserEntity fromDomainUser(User user) {
    	return new UserEntity(user.uid(), user.displayName(), user.profilePictureUrl(), user.devices(), user.connections(), user.blocklist(), user.status(), user.lastHeartbeat());
    }

    public User toDomainUser() {
        return new User(uid, displayName, profilePictureUrl, devices, connections, blocklist, status, lastHeartbeat);
    }

    public String uid() {
        return uid;
    }

    public List<Device> devices() {
        return devices;
    }

    public List<String> blocklist() {
        return blocklist;
    }

    public UserStatus status() {
        return status;
    }

    public List<String> connections() {
        return connections;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public long lastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserEntity) obj;
        return Objects.equals(this.uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, displayName, profilePictureUrl, devices, connections, blocklist, status, lastHeartbeat);
    }

    @Override
    public String toString() {
        return "UserEntity[" +
            "uid=" + uid + ", " +
            "displayName=" + displayName + ", " +
            "profilePictureUrl=" + profilePictureUrl + ", " +
            "fcmTokens=" + devices + ", " +
            "connections=" + connections + ", " +
            "blocklist=" + blocklist + ", " +
            "status=" + status + ", " +
            "lastHeartbeat=" + lastHeartbeat + ']';
    }

}