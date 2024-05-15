package pl.lodz.p.it.ssbd2024.ssbd01.mock.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebCoreConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.mock.TestServiceConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;

import java.util.ArrayList;
import java.util.List;

@SpringJUnitWebConfig(classes = {TestServiceConfig.class})
public class AccountServiceMockTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMokRepository accountMokRepository;

    @BeforeEach
    public void setup() {
        var accountOne = new Account("admin", "admin", 1);
        var accountTwo = new Account("admin2", "admin2", 1);
        var accountThree = new Account("admin3", "admin3", 1);

        List<Account> accountList = new ArrayList<>() {{
            add(accountOne);
            add(accountTwo);
            add(accountThree);
        }};

        Mockito.when(accountMokRepository.findAll()).thenReturn(accountList);
    }

//    @Test
//    void testFindAllAccounts() {
//        var accounts = accountService.getAllAccounts();
//        Assertions.assertEquals(3, accounts.size());
//    }

}
