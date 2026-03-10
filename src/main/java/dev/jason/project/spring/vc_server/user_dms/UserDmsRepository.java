package dev.jason.project.spring.vc_server.user_dms;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDmsRepository extends MongoRepository<UserDms, String> {
    UserDms findByUid(String uid);
}
