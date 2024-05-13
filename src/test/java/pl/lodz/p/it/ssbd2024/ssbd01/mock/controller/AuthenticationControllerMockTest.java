package pl.lodz.p.it.ssbd2024.ssbd01.mock.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.controller.AuthenticationController;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.service.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebCoreConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.mock.TestConfig;

@SpringJUnitWebConfig(classes = {WebCoreConfig.class, TestConfig.class})
public class AuthenticationControllerMockTest {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private AuthenticationService authenticationService;

    private MockMvc mockMvcAuthentication;

    @BeforeEach
    public void setup() {
        mockMvcAuthentication = MockMvcBuilders.standaloneSetup(authenticationController).build();

    }

}
