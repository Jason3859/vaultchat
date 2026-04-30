package dev.jason.project.spring.vc_server.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import dev.jason.project.spring.vc_server.dto.UserDto;
import dev.jason.project.spring.vc_server.repo.db.user.UserEntity;
import dev.jason.project.spring.vc_server.repo.db.user.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UserSocialConnectionsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

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
    void connecting_two_existing_users_returns_accepted() throws Exception {
        mockMvc.perform(patch("/social/connect")
                .param("from_uid", TestConstants.TEST_USER_1.uid())
                .param("other_uid", TestConstants.TEST_USER_2.uid()))
            .andExpect(status().isAccepted());
    }

    @Test
    void connecting_with_unknown_user_returns_not_found() throws Exception {
        mockMvc.perform(patch("/social/connect")
                .param("from_uid", TestConstants.TEST_USER_1.uid())
                .param("other_uid", "unknown"))
            .andExpect(status().isNotFound());
    }

    @Test
    void connecting_users_where_one_blocked_other_returns_forbidden() throws Exception {
        mockMvc.perform(patch("/social/block")
                .param("from_uid", TestConstants.TEST_USER_2.uid())
                .param("other_uid", TestConstants.TEST_USER_1.uid()));

        mockMvc.perform(patch("/social/connect")
                .param("from_uid", TestConstants.TEST_USER_1.uid())
                .param("other_uid", TestConstants.TEST_USER_2.uid()))
            .andExpect(status().isForbidden());
    }

    @Test
    void blocking_user_returns_accepted() throws Exception {
        mockMvc.perform(patch("/social/block")
                .param("from_uid", TestConstants.TEST_USER_1.uid())
                .param("other_uid", TestConstants.TEST_USER_2.uid()))
            .andExpect(status().isAccepted());
    }

    @Test
    void unblocking_user_that_was_not_blocked_returns_not_found() throws Exception {
        mockMvc.perform(patch("/social/unblock")
                .param("from_uid", TestConstants.TEST_USER_1.uid())
                .param("other_uid", TestConstants.TEST_USER_2.uid()))
            .andExpect(status().isNotFound());
    }

    @Test
    void search_users_returns_ok() throws Exception {
        mockMvc.perform(get("/social/search")
                .param("from_uid", TestConstants.TEST_USER_1.uid())
                .param("search_query", "Test"))
            .andExpect(status().isOk());
    }
    
    @Test
    void blocking_same_user_returns_not_acceptable() throws Exception {
    	mockMvc.perform(patch("/social/block")
    			.param("from_uid", TestConstants.TEST_USER_1.uid())
    			.param("other_uid", TestConstants.TEST_USER_1.uid()))
    		.andExpect(status().isNotAcceptable());
    }
    
    @Test
    void unblocking_same_user_returns_not_acceptable() throws Exception {
    	mockMvc.perform(patch("/social/unblock")
    			.param("from_uid", TestConstants.TEST_USER_1.uid())
    			.param("other_uid", TestConstants.TEST_USER_1.uid()))
    		.andExpect(status().isNotAcceptable());
    }
    
    @Test
    void searching_for_users_as_unknown_user_returns_not_found() throws Exception {
    	mockMvc.perform(get("/social/search")
    			.param("from_uid", "Unknown")
    			.param("search_query", "Jason"))
    		.andExpect(status().isNotFound());
    }
}
