package pl.lodz.p.it.ssbd2024.ssbd01.mock.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.AccountAuthRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.service.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebCoreConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.mock.TestServiceConfig;

@SpringJUnitWebConfig(classes = {TestServiceConfig.class})
public class AuthenticationServiceMockTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AccountAuthRepository accountAuthRepository;

    private MockMvc mockMvcAuthentication;

    @BeforeEach
    public void setup() {
        mockMvcAuthentication = MockMvcBuilders.standaloneSetup(authenticationService).build();

    }

}
