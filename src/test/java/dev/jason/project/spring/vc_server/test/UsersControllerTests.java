package dev.jason.project.spring.vc_server.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
public class UsersControllerTests {

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
    void adding_existing_user_with_same_device_returns_conflict() throws Exception {
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestConstants.TEST_USER_1)))
            .andExpect(status().isConflict());
    }

    @Test
    void adding_new_user_returns_created() throws Exception {
        UserDto userDto = new UserDto(
            TestConstants.UNKNOWN_USER_1.uid(),
            TestConstants.UNKNOWN_USER_1.displayName(),
            TestConstants.UNKNOWN_USER_1.profilePictureUrl(),
            DeviceDto.fromDomain(TestConstants.TEST_DEVICE_1)
        );

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
            .andExpect(status().isCreated());

        // Cleanup
        repository.deleteById(TestConstants.UNKNOWN_USER_1.uid());
    }

    @Test
    void deleting_user_returns_accepted() throws Exception {
        mockMvc.perform(delete("/user/delete")
                .param("uid", TestConstants.TEST_USER_1.uid()))
            .andExpect(status().isAccepted());
    }

    @Test
    void deleting_unknown_user_returns_not_found() throws Exception {
        mockMvc.perform(delete("/user/delete")
                .param("uid", "unknown"))
            .andExpect(status().isNotFound());
    }
}
