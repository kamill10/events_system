package pl.lodz.p.it.ssbd2024.ssbd01.mock.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebCoreConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.mock.TestConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;

@SpringJUnitWebConfig(classes = {WebCoreConfig.class, TestConfig.class})
public class AccountServiceMockTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMokRepository accountMokRepository;

    private MockMvc mockMvcAccount;

    @BeforeEach
    public void setup() {
        mockMvcAccount = MockMvcBuilders.standaloneSetup(accountService).build();

    }

}
