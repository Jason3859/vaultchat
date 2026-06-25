package dev.jason.project.spring.vc_server.social_microservice.repo;

import dev.jason.project.spring.vc_server.social_microservice.model.SocialEntity;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepository extends MongoRepository<SocialEntity, String> {

	Optional<SocialEntity> findByUserId(String userId);
}
