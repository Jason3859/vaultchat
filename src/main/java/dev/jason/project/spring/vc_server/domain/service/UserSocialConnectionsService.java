package dev.jason.project.spring.vc_server.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.domain.exception.BlockedByUserException;
import dev.jason.project.spring.vc_server.domain.exception.NoUsersBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserAlreadyBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.exception.UsersAlreadyConnectedException;
import dev.jason.project.spring.vc_server.domain.model.User;
import dev.jason.project.spring.vc_server.domain.repo.db.user.UserEntity;

@Service
public class UserSocialConnectionsService extends UserService {
	
    public void addConnection(User user, User otherUser) throws UserNotFoundException, UsersAlreadyConnectedException, BlockedByUserException {
        UserEntity entity1 = getUserEntityOrThrow(user.uid());
        UserEntity entity2 =  getUserEntityOrThrow(otherUser.uid());

        if (entity1.connections().contains(otherUser.uid())) {
            throw new UsersAlreadyConnectedException();
        }
        
        // if user is blocked
        if (entity1.blocklist().contains(otherUser.uid()) || entity2.blocklist().contains(user.uid())) {
        	throw new BlockedByUserException();
        }

        entity1.connections().add(otherUser.uid());
        entity2.connections().add(user.uid());

        userRepository.saveAll(List.of(entity1, entity2));
    }

    public void block(String uid1, String uid2) throws UserNotFoundException, UserAlreadyBlockedException {
        UserEntity entity1 = getUserEntityOrThrow(uid1);
        UserEntity entity2 = getUserEntityOrThrow(uid2);

        if (entity1.blocklist().contains(uid2)) {
            throw new UserAlreadyBlockedException();
        }

        entity1.blocklist().add(uid2);
        entity1.connections().remove(uid2);

        entity2.blocklist().add(uid1);
        entity2.connections().remove(uid1);

        userRepository.saveAll(List.of(entity1, entity2));
    }

    public void unblock(String uid1, String uid2) throws UserNotFoundException, UserNotBlockedException, NoUsersBlockedException {
        UserEntity entity1 = getUserEntityOrThrow(uid1);
        UserEntity entity2 = getUserEntityOrThrow(uid2);

        if (entity1.blocklist().isEmpty()) {
            throw new NoUsersBlockedException();
        }

        if (entity1.blocklist().contains(uid2)) {
            entity1.blocklist().remove(uid2);
            entity2.blocklist().remove(uid1);

            entity1.connections().add(entity2.uid());
            entity2.connections().add(entity1.uid());

            userRepository.saveAll(List.of(entity1, entity2));
        } else throw new UserNotBlockedException();
    }

}
