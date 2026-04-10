package dev.jason.project.spring.vc_server;

import dev.jason.project.spring.vc_server.domain.Device;
import dev.jason.project.spring.vc_server.domain.UserStatus;
import dev.jason.project.spring.vc_server.dto.DeviceDto;
import dev.jason.project.spring.vc_server.dto.RegisterUserDto;
import dev.jason.project.spring.vc_server.dto.ResultDto;
import dev.jason.project.spring.vc_server.dto.UserDto;
import dev.jason.project.spring.vc_server.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static dev.jason.project.spring.vc_server.TestConstants.TEST_USER_1;
import static dev.jason.project.spring.vc_server.TestConstants.TEST_USER_2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setup() throws Exception {
        registerUser(TEST_USER_1, "User One");
        registerUser(TEST_USER_2, "User Two");
    }

    @AfterEach
    void tearDown() {
        repository.deleteById(TEST_USER_1);
        repository.deleteById(TEST_USER_2);
    }

    private void registerUser(String uid, String displayName) throws Exception {
        RegisterUserDto dto = new RegisterUserDto(uid, displayName, null, null);
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void searchUsers_Success() throws Exception {
        mockMvc.perform(get("/user/search")
                .param("name", "User")
                .param("from", TEST_USER_1))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.Success, List.of(new UserDto(TEST_USER_2, "User Two", null, UserStatus.Online))))));
    }

    @Test
    void searchUsers_NoResults() throws Exception {
        mockMvc.perform(get("/user/search")
                .param("name", "NonExistent")
                .param("from", TEST_USER_1))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.NoUsersFound))));
    }

    @Test
    void getConnections_Empty() throws Exception {
        mockMvc.perform(get("/user/get-connections")
                .param("uid", TEST_USER_1))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.Success, List.of()))));
    }

    @Test
    void heartbeat_Success() throws Exception {
        mockMvc.perform(put("/user/heartbeat")
                .param("uid", TEST_USER_1))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.Success))));
    }

    @Test
    void heartbeat_UserNotFound() throws Exception {
        mockMvc.perform(put("/user/heartbeat")
                .param("uid", "unknown"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.UserNotFound))));
    }

    @Test
    void addDevice_Success() throws Exception {
        DeviceDto deviceDto = new DeviceDto("My Phone", Device.Type.Mobile, Device.OS.Android, "13", "fcm-token");
        mockMvc.perform(post("/devices/add")
                .param("uid", TEST_USER_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceDto)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.Success))));
    }

    @Test
    void getMyDevices_Success() throws Exception {
        DeviceDto deviceDto = new DeviceDto("My Phone", Device.Type.Mobile, Device.OS.Android, "13", "fcm-token");
        mockMvc.perform(post("/devices/add")
                .param("uid", TEST_USER_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceDto)))
            .andExpect(status().isOk());

        mockMvc.perform(get("/devices/my-devices")
                .param("uid", TEST_USER_1))
            .andExpect(status().isOk())
            .andExpect(content().json("{\"result\":\"Success\"}"));
    }

    @Test
    void deleteDevice_Success() throws Exception {
        DeviceDto deviceDto = new DeviceDto("My Phone", Device.Type.Mobile, Device.OS.Android, "13", "fcm-token");
        mockMvc.perform(post("/devices/add")
                .param("uid", TEST_USER_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceDto)))
            .andExpect(status().isOk());

        mockMvc.perform(delete("/devices/delete")
                .param("uid", TEST_USER_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceDto)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.Success))));
    }

    @Test
    void updateToken_Success() throws Exception {
        DeviceDto deviceDto = new DeviceDto("My Phone", Device.Type.Mobile, Device.OS.Android, "13", "fcm-token");
        mockMvc.perform(post("/devices/add")
                .param("uid", TEST_USER_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceDto)))
            .andExpect(status().isOk());

        mockMvc.perform(post("/user/update-token")
                .param("uid", TEST_USER_1)
                .param("token", "new-fcm-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceDto)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.Success))));
    }
}
