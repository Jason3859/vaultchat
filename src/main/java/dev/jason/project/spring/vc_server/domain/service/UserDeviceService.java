package dev.jason.project.spring.vc_server.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.domain.exception.DeviceAlreadyExistsException;
import dev.jason.project.spring.vc_server.domain.exception.DeviceNotFoundException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.model.Device;
import dev.jason.project.spring.vc_server.domain.repo.db.user.UserEntity;

@Service
public class UserDeviceService extends UserService {
	
    public void addDevice(String uid, Device device) throws UserNotFoundException, DeviceAlreadyExistsException {
        UserEntity entity = getUserEntityOrThrow(uid);
        List<Device> devices = entity.devices();

        for (Device d : devices) {
            if (d.equals(device)) {
                throw new DeviceAlreadyExistsException();
            }
        }

        entity.devices().add(device);
        userRepository.save(entity);
    }

    public void deleteDevice(String uid, Device device) throws UserNotFoundException, DeviceNotFoundException {
        UserEntity entity = getUserEntityOrThrow(uid);
        List<Device> devices = entity.devices();

        for (Device d : devices) {
            if (d.equals(device)) {
                devices.remove(d);
                userRepository.save(entity);
                return;
            }
        }

        throw new DeviceNotFoundException();
    }

    public List<Device> getUserDevicesByUid(String uid) throws UserNotFoundException {
        return getUserEntityOrThrow(uid).devices();
    }

    public void updateToken(String uid, String token, Device device) throws UserNotFoundException, DeviceNotFoundException {
        UserEntity entity = getUserEntityOrThrow(uid);
        List<Device> devices = entity.devices();

        for (Device d : devices) {
            if (d.equals(device)) {
                d.setFcmToken(token);
                userRepository.save(entity);
                return;
            }
        }

        throw new DeviceNotFoundException();
    }
}
