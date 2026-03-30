package dev.jason.project.spring.vc_server;

import dev.jason.project.spring.vc_server.dto.ResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import static dev.jason.project.spring.vc_server.TestConstants.TEST_USER_1;
import static dev.jason.project.spring.vc_server.TestConstants.TEST_USER_2;

@SpringBootTest
@AutoConfigureMockMvc
public class VCSpringAppTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getConnectionsForUnknownUser() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/get-connections")
                .param("uid", TEST_USER_1)
        )
            .andExpect(
                MockMvcResultMatchers.content().json(
                    objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.UserNotFound))
                )
            );
    }

    @Test
    void searchForUnknownUser() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/search-users")
                .param("name", TEST_USER_1)
                .param("from", TEST_USER_2)
        )
            .andExpect(
                MockMvcResultMatchers.content().json(
                    objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.NoUsersFound))
                )
            );
    }

    @Test
    void getBlocklistForUnknownUser() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/get-blocked-users")
                .param("uid", TEST_USER_1)
        )
            .andExpect(
                MockMvcResultMatchers.content().json(
                    objectMapper.writeValueAsString(new ResultDto(ResultDto.Result.UserNotFound))
                )
            );
    }
}
