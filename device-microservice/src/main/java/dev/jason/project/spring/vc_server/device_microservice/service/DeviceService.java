package dev.jason.project.spring.vc_server.device_microservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.core.exception.VcException.DeviceException.DeviceAlreadyExistsException;
import dev.jason.project.spring.vc_server.core.exception.VcException.DeviceException.DeviceNotFoundException;
import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserNotFoundException;
import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.device_microservice.client.UserClient;
import dev.jason.project.spring.vc_server.device_microservice.model.DeviceEntity;
import dev.jason.project.spring.vc_server.device_microservice.repo.DeviceRepository;
import feign.FeignException;

@Service
public class DeviceService {

	@Autowired
	private DeviceRepository deviceRepository;
	
	@Autowired
	private UserClient userClient;
	
	public List<Device> getDevicesByOwner(String id) {
		return deviceRepository.findByOwnerUid(id).stream()
			.map(DeviceEntity::asDevice)
			.toList();
	}
	
	public Device addDevice(Device device) {
		
		try {
			userClient.getUserByUid(device.getOwnerUid());
		} catch (FeignException.NotFound e) {
			throw new UserNotFoundException();
		}
		
		List<DeviceEntity> devices = deviceRepository.findByOwnerUid(device.getOwnerUid());
		
		DeviceEntity entity = DeviceEntity.asEntity(device);
		
		if (devices.contains(entity)) {
			throw new DeviceAlreadyExistsException();
		}
		
		deviceRepository.save(entity);
		
		return entity.asDevice();
	}
	
	public void deleteDevice(Device device) {
		List<DeviceEntity> entities = deviceRepository.findByOwnerUid(device.getOwnerUid());
		
		if (entities.contains(DeviceEntity.asEntity(device))) {			
			deviceRepository.delete(DeviceEntity.asEntity(device));
			return;
		}
		
		throw new DeviceNotFoundException();
	}
}
