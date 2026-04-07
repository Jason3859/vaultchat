package dev.jason.project.spring.vc_server.users;

import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.domain.UserFcmToken;

@Document(collection = "users")
public final class UserEntity {
    @Id
    private final String uid;
    private final String displayName;
    private final String profilePictureUrl;
    private final List<UserFcmToken> fcmTokens;
    private List<String> connections;
    private List<String> blocklist;

    public UserEntity(
        String uid,
        String displayName,
        String profilePictureUrl,
        List<UserFcmToken> fcmTokens,
        List<String> connections,
        List<String> blocklist
    ) {
        this.uid = uid;
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
        this.fcmTokens = fcmTokens;
        this.connections = connections;
        this.blocklist = blocklist;
    }
    
    public static UserEntity fromDomainUser(User user) {
    	return new UserEntity(user.uid(), user.displayName(), user.profilePictureUrl(), user.fcmTokens(), user.connections(), user.blocklist());
    }

    public User toDomainUser() {
        return new User(uid, displayName, profilePictureUrl, fcmTokens, connections, blocklist);
    }

    public String uid() {
        return uid;
    }

    public List<UserFcmToken> fcmTokens() {
        return fcmTokens;
    }

    public List<String> blocklist() {
        return blocklist;
    }

    public void setConnections(List<String> connections) {
        this.connections = connections;
    }

    public void setBlocklist(List<String> blocklist) {
        this.blocklist = blocklist;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserEntity) obj;
        return Objects.equals(this.uid, that.uid) &&
            Objects.equals(this.displayName, that.displayName) &&
            Objects.equals(this.profilePictureUrl, that.profilePictureUrl) &&
            Objects.equals(this.fcmTokens, that.fcmTokens) &&
            Objects.equals(this.connections, that.connections) &&
            Objects.equals(this.blocklist, that.blocklist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, displayName, profilePictureUrl, fcmTokens, connections, blocklist);
    }

    @Override
    public String toString() {
        return "UserEntity[" +
            "uid=" + uid + ", " +
            "displayName=" + displayName + ", " +
            "profilePictureUrl=" + profilePictureUrl + ", " +
            "fcmTokens=" + fcmTokens + ", " +
            "connections=" + connections + ", " +
            "blocklist=" + blocklist + ']';
    }

}