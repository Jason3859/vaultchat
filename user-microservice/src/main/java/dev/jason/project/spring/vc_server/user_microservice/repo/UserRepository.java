package dev.jason.project.spring.vc_server.user_microservice.repo;

import dev.jason.project.spring.vc_server.user_microservice.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    List<UserEntity> findByDisplayNameContainingIgnoreCase(String displayName);
}
