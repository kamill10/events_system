package pl.lodz.p.it.ssbd2024.ssbd01.mock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.controller.AuthenticationController;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.service.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebCoreConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.mock.TestControllerConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.mock.TestServiceConfig;

import javax.security.auth.login.AccountLockedException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = {TestControllerConfig.class})
public class AuthenticationControllerMockTest {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private AuthenticationService authenticationService;

    private MockMvc mockMvcAuthentication;

    static ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws AccountLockedException {
        mockMvcAuthentication = MockMvcBuilders.standaloneSetup(authenticationController).build();
        var adminLoginDTO = new LoginDTO("admin", "admin");
        Account account = null;
        Mockito.when(authenticationService.authenticate(adminLoginDTO, "en-US")).thenReturn("secr3t_token");
    }

    public static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Test
    void testAdminAuthentication() throws Exception {
        var adminLoginDTO = new LoginDTO("admin", "admin");
        var tokenFromService = mockMvcAuthentication.perform(post("/api/auth/authenticate")
                        .header("Accept-Language", "en-US")
                .contentType("application/json")
                .content(objectMapper().writeValueAsString(adminLoginDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertEquals("secr3t_token", tokenFromService);
    }

}
