package pl.lodz.p.it.ssbd2024.ssbd01;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.controllers.AuthenticationController;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.controllers.AccountController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        MvcResult result = mockMvcAuth.perform(post("/api/auth/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"password\",\"email\":\"email@email.com\",\"gender\":\"0\",\"firstName\":\"firstName\",\"lastName\":\"lastName\"}"))
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
