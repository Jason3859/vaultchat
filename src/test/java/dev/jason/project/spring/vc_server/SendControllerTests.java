package dev.jason.project.spring.vc_server;

import com.google.firebase.messaging.FirebaseMessaging;
import dev.jason.project.spring.vc_server.domain.Message;
import dev.jason.project.spring.vc_server.dto.RegisterUserDto;
import dev.jason.project.spring.vc_server.dto.ResultDto;
import dev.jason.project.spring.vc_server.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static dev.jason.project.spring.vc_server.TestConstants.TEST_USER_1;
import static dev.jason.project.spring.vc_server.TestConstants.TEST_USER_2;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SendControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setup() throws Exception {
        registerUser(TEST_USER_1);
        registerUser(TEST_USER_2);
    }

    @AfterEach
    void tearDown() {
        repository.deleteById(TEST_USER_1);
        repository.deleteById(TEST_USER_2);
    }

    private void registerUser(String uid) throws Exception {
        RegisterUserDto dto = new RegisterUserDto(uid);
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void send_Success() throws Exception {
        FirebaseMessaging firebaseMessaging = mock(FirebaseMessaging.class);
        try (MockedStatic<FirebaseMessaging> mocked = mockStatic(FirebaseMessaging.class)) {
            mocked.when(FirebaseMessaging::getInstance).thenReturn(firebaseMessaging);

            Message message = new Message(TEST_USER_1, TEST_USER_2, "Hello", "now");
            mockMvc.perform(post("/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.Success))));
        }
    }

    @Test
    void send_MessageTextBlank() throws Exception {
        Message message = new Message(TEST_USER_1, TEST_USER_2, " ", "now");
        mockMvc.perform(post("/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(message)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.MessageTextBlank))));
    }

    @Test
    void send_UserNotFound() throws Exception {
        Message message = new Message(TEST_USER_1, "unknown", "Hello", "now");
        mockMvc.perform(post("/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(message)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.UserNotFound))));
    }

    @Test
    void notifyFcmTokenDeletion_Success() throws Exception {
        FirebaseMessaging firebaseMessaging = mock(FirebaseMessaging.class);
        try (MockedStatic<FirebaseMessaging> mocked = mockStatic(FirebaseMessaging.class)) {
            mocked.when(FirebaseMessaging::getInstance).thenReturn(firebaseMessaging);

            mockMvc.perform(post("/send/notify-fcm-token-deletion")
                    .param("token", "dummy-token"))
                .andExpect(status().isOk());

            verify(firebaseMessaging, times(1)).send(any());
        }
    }
}
