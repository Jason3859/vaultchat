package dev.jason.project.spring.vc_server.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import dev.jason.project.spring.vc_server.dto.DeviceDto;
import dev.jason.project.spring.vc_server.dto.UserDto;
import dev.jason.project.spring.vc_server.repo.db.user.UserEntity;
import dev.jason.project.spring.vc_server.repo.db.user.UserRepository;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserDeviceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        repository.saveAll(
            Stream.of(TestConstants.TEST_USER_1, TestConstants.TEST_USER_2)
                .map(UserDto::asNewUser)
                .map(UserEntity::fromDomainUser).toList()
        );
    }

    @AfterEach
    void delete_test_users() {
        repository.deleteAll(
            Stream.of(TestConstants.TEST_USER_1, TestConstants.TEST_USER_2)
                .map(UserDto::asNewUser)
                .map(UserEntity::fromDomainUser).toList()
        );
    }

    @Test
    void adding_device_for_unknown_user_returns_not_found() throws Exception {
        DeviceDto deviceDto = DeviceDto.fromDomain(TestConstants.TEST_DEVICE_1);
        mockMvc.perform(post("/user/devices/add")
                .param("uid", "unknown")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceDto)))
            .andExpect(status().isNotFound());
    }

    @Test
    void adding_device_that_is_already_registered_returns_conflict() throws Exception {
        DeviceDto deviceDto = DeviceDto.fromDomain(TestConstants.TEST_DEVICE_1);
        mockMvc.perform(post("/user/devices/add")
                .param("uid", TestConstants.TEST_USER_1.uid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceDto)))
            .andExpect(status().isConflict());
    }

    @Test
    void adding_device_for_known_user_returns_created() throws Exception {
        DeviceDto deviceDto = DeviceDto.fromDomain(TestConstants.TEST_DEVICE_2);
        mockMvc.perform(post("/user/devices/add")
                .param("uid", TestConstants.TEST_USER_1.uid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceDto)))
            .andExpect(status().isCreated());
    }

    @Test
    void deleting_registered_device_for_known_user_returns_accepted() throws Exception {
        DeviceDto deviceDto = DeviceDto.fromDomain(TestConstants.TEST_DEVICE_1);
        mockMvc.perform(delete("/user/devices/delete")
                .param("uid", TestConstants.TEST_USER_1.uid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceDto)))
            .andExpect(status().isAccepted());
    }

    @Test
    void deleting_unregistered_device_for_known_user_returns_not_found() throws Exception {
        DeviceDto deviceDto = DeviceDto.fromDomain(TestConstants.TEST_DEVICE_2);
        mockMvc.perform(delete("/user/devices/delete")
                .param("uid", TestConstants.TEST_USER_1.uid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceDto)))
            .andExpect(status().isNotFound());
    }

    @Test
    void updating_token_of_registered_device_returns_accepted() throws Exception {
        DeviceDto deviceDto = DeviceDto.fromDomain(TestConstants.TEST_DEVICE_1);
        mockMvc.perform(patch("/user/devices/update-token")
                .param("uid", TestConstants.TEST_USER_1.uid())
                .param("newToken", "new-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceDto)))
            .andExpect(status().isAccepted());
    }
}
