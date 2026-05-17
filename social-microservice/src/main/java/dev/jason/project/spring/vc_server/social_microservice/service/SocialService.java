package dev.jason.project.spring.vc_server.social_microservice.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.core.exception.VcException.SocialException.SelfBlockException;
import dev.jason.project.spring.vc_server.core.exception.VcException.SocialException.SelfUnblockException;
import dev.jason.project.spring.vc_server.core.exception.VcException.SocialException.UserNotBlockedException;
import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserAlreadyExistsException;
import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserNotFoundException;
import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.social_microservice.model.SocialEntity;
import dev.jason.project.spring.vc_server.social_microservice.repo.SocialRepository;

@Service
public class SocialService {

    @Autowired
    private SocialRepository repository;
    
	public void registerNewUser(String uid) {
		if (repository.findByUserId(uid) != null) {
			throw new UserAlreadyExistsException();
		}
		
		SocialEntity entity = new SocialEntity(uid, List.of(), List.of(), List.of());
		repository.save(entity);
	}

    public void block(String uid1, String uid2) {
        if (Objects.equals(uid1, uid2)) {
            throw new SelfBlockException();
        }

        SocialEntity entity1 = getSocialEntityByUid(uid1);
        SocialEntity entity2 = getSocialEntityByUid(uid2);

        if (entity1 == null || entity2 == null) {
            throw new UserNotFoundException();
        }

        entity1.getBlocklist().add(uid2);
        entity1.getConnections().remove(uid2);

        repository.save(entity1);
    }

    public void unblock(String uid1, String uid2) {
        if (Objects.equals(uid1, uid2)) {
            throw new SelfUnblockException();
        }

        SocialEntity entity1 = getSocialEntityByUid(uid1);
        SocialEntity entity2 = getSocialEntityByUid(uid2);

        if (entity1 == null || entity2 == null) {
            throw new UserNotFoundException();
        }

        List<String> blocklist = entity1.getBlocklist();

        if (blocklist.contains(uid2)) {
            blocklist.remove(uid2);
            repository.save(entity1);
            return;
        }

        throw new UserNotBlockedException();
    }

    public void addConnection(String uid1, String uid2) {
        SocialEntity entity1 = getSocialEntityByUid(uid1);
        SocialEntity entity2 = getSocialEntityByUid(uid2);

        if (entity1.getConnections().contains(uid2)) {
            return;
        }

        entity1.getConnections().add(uid2);
        entity2.getConnections().add(uid1);

        repository.save(entity1);
        repository.save(entity2);
    }

    public SocialEntity getSocialEntityByUid(String uid) {
        SocialEntity entity = repository.findByUserId(uid);

        if (entity == null) {
            throw new UserNotFoundException();
        }

        return entity;
    }

	public void addMessageToQueue(String uid, Message message) {
		SocialEntity entity = getSocialEntityByUid(uid);
		
		entity.getQueuedMessages().add(message);
	
		repository.save(entity);
	}
}
