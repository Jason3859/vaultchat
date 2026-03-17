package dev.jason.project.spring.vc_server;

import dev.jason.project.spring.vc_server.domain.Message;
import dev.jason.project.spring.vc_server.dto.AddUserDto;
import dev.jason.project.spring.vc_server.dto.ResultDto;
import dev.jason.project.spring.vc_server.users.UserDbRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserBlocklistTests {

    private static final String TEST_USER_1 = "test-user-1";
    private static final String TEST_USER_2 = "test-user-2";
    private static final String TEST_USER_TOKEN_1 = "token-1";
    private static final String TEST_USER_TOKEN_2 = "token-2";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDbRepository repository;

    @Test
    @Order(1)
    void addTestUser1() throws Exception {
        mvc.perform(
            post("/add-user")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new AddUserDto(TEST_USER_1, TEST_USER_TOKEN_1)))
        )
            .andExpect(content().string(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.Success))));
    }

    @Test
    @Order(2)
    void addTestUser2() throws Exception {
        mvc.perform(
                post("/add-user")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(new AddUserDto(TEST_USER_2, TEST_USER_TOKEN_2)))
            )
            .andExpect(content().string(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.Success))));
    }

    @Test
    @Order(3)
    void unblockUser1FromUser2BeforeBlocking() throws Exception {
        mvc.perform(
            post("/unblock-user")
                .param("uid", TEST_USER_1)
                .param("uid_to_unblock", TEST_USER_2)
        )
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.UserNotBlocked))));
    }

    @Test
    @Order(4)
    void blockUser1FromUser2() throws Exception {
        mvc.perform(
            post("/block-user")
                .param("uid", TEST_USER_2)
                .param("uid_to_block", TEST_USER_1)
        )
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.Success))));
    }

    @Test
    @Order(5)
    void blockUser1AgainFromUser2() throws Exception {
        mvc.perform(
            post("/block-user")
                .param("uid", TEST_USER_2)
                .param("uid_to_block", TEST_USER_1)
        )
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.AlreadyBlocked))));
    }

    @Test
    @Order(6)
    void sendMessageFromUser2ToUser1() throws Exception {
        mvc.perform(
            post("/send")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new Message(TEST_USER_2, TEST_USER_1, "hello", "hai")))
        )
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.BlockedByUser))));
    }

    @Test
    @Order(7)
    void sendMessageFromUser1ToUser2() throws Exception {
        mvc.perform(
                post("/send")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(new Message(TEST_USER_1, TEST_USER_2, "hello", "hai")))
            )
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.BlockedByUser))));
    }

    @Test
    @Order(8)
    void unblockUser1FromUser2() throws Exception {
        mvc.perform(
                post("/unblock-user")
                    .param("uid", TEST_USER_2)
                    .param("uid_to_unblock", TEST_USER_1)
            )
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.Success))));
    }

    @Test
    @Order(9)
    void unblockUser1FromUser2Again() throws Exception {
        mvc.perform(
                post("/unblock-user")
                    .param("uid", TEST_USER_2)
                    .param("uid_to_unblock", TEST_USER_1)
            )
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.UserNotBlocked))));
    }

    @Test
    @Order(10)
    void blockUser1FromUser1() throws Exception {
        mvc.perform(
            post("/block-user")
                .param("uid", TEST_USER_1)
                .param("uid_to_block", TEST_USER_1)
        )
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.SelfBlock))));
    }

    @Test
    @Order(11)
    void unblockUser1FromUser1() throws Exception {
        mvc.perform(
            post("/unblock-user")
                .param("uid", TEST_USER_1)
                .param("uid_to_unblock", TEST_USER_1)
        )
            .andExpect(content().json(objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.SelfUnblock))));
    }

    @Test
    @Order(12)
    void deleteTestUsers() {
        repository.deleteById(TEST_USER_1);
        repository.deleteById(TEST_USER_2);
    }

    @Test
    @Order(13)
    void getBlockedUsers() throws Exception {
        mvc.perform(
            MockMvcRequestBuilders.get("/get-blocked-users")
                .param("uid", TEST_USER_1)
        )
            .andExpect(content().json(objectMapper.writeValueAsString(new ArrayList<>(List.of()))));
    }
}
