package dev.jason.project.spring.vc_server.data.db.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserEntity findByUid(String uid);
    List<UserEntity> findByDisplayNameContainingIgnoreCase(String displayName);
}
