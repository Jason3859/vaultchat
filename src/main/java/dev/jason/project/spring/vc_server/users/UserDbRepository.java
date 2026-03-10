package dev.jason.project.spring.vc_server.users;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserDbRepository extends MongoRepository<DBUser, String> {
    DBUser findByUid(String uid);
    List<DBUser> findByDisplayName(String displayName);
}
