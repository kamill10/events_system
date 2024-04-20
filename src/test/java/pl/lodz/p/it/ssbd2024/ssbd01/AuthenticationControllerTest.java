package pl.lodz.p.it.ssbd2024.ssbd01;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.controller.AuthenticationController;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.controller.AccountController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringJUnitWebConfig(classes = {WebConfig.class})
public class AuthenticationControllerTest {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private AccountController accountController;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    private MockMvc mockMvcAuth;

    private MockMvc mockMvcAccount;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16.2"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("jdbc.url", postgres::getJdbcUrl);
        registry.add("jdbc.admin.user", postgres::getUsername);
        registry.add("jdbc.admin.password", postgres::getPassword);
        registry.add("jdbc.mok.user", postgres::getUsername);
        registry.add("jdbc.mok.password", postgres::getPassword);
        registry.add("jdbc.auth.user", postgres::getUsername);
        registry.add("jdbc.auth.password", postgres::getPassword);
    }

    @BeforeEach
    void setup() {

        this.mockMvcAuth = MockMvcBuilders
                .standaloneSetup(authenticationController)
                .setHandlerExceptionResolvers(handlerExceptionResolver)
                .build();

        this.mockMvcAccount = MockMvcBuilders
                .standaloneSetup(accountController)
                .setHandlerExceptionResolvers(handlerExceptionResolver)
                .build();
    }

    @Test
    public void testAuthenticationEndpoint() throws Exception {

        MvcResult result = mockMvcAuth.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"password\",\"email\":\"email@email.com\",\"gender\":\"0\"," +
                                "\"firstName\":\"firstName\",\"lastName\":\"lastName\"}"))
                .andExpect(status().isOk())
                .andReturn();

        mockMvcAuth.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"password\"}"))
                .andExpect(status().isOk());

        String content = result.getResponse().getContentAsString();

        MvcResult result2 = mockMvcAccount.perform(get("/api/accounts")
                        .header("Authorization", "Bearer " + content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content2 = result2.getResponse().getContentAsString();

        Assertions.assertTrue(content2.contains("user"));
    }
}
