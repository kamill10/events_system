package pl.lodz.p.it.ssbd2024.ssbd01.mock;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebCoreConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.LanguageEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.controller.AccountController;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;

import java.util.ArrayList;
import java.util.List;

@SpringJUnitWebConfig(classes = {WebCoreConfig.class})
class AccountControllerMock {

    @Mock
    private AccountService accountServiceMock;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvcAccount;


    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(accountServiceMock.getAllAccounts()).thenReturn(new ArrayList<>() {
            {
                add(new Account("login1", "password1", "email1@email.com", 1, "firstname1", "lastname2", LanguageEnum.POLISH));
                add(new Account("login2", "password2", "email1@email.com", 0, "firstname2", "lastname2", LanguageEnum.ENGLISH));
            }
        });
    }

    @BeforeEach
    void setup() {
        this.mockMvcAccount = MockMvcBuilders
                .standaloneSetup(accountController)
                .build();

    }


    @Test
    void testReadAllAccounts() {
        List<Account> accounts = accountServiceMock.getAllAccounts();
        List<GetAccountDTO> accountsFromController = accountController.getAllUsers();
        Assertions.assertEquals(accounts.size(), accountsFromController.size());
    }


}
