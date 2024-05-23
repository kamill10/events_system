package pl.lodz.p.it.ssbd2024.ssbd01.mock.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.mock.TestServiceConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.RoleRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringJUnitWebConfig(classes = {TestServiceConfig.class})
public class AccountServiceMockTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMokRepository accountMokRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Account accountOne;
    private Account accountTwo;
    private Account accountThree;

    @BeforeEach
    public void setup() {
        accountOne = new Account("admin", "admin", 1);
        accountTwo = new Account("admin2", "admin2", 1);
        accountThree = new Account("admin3", "admin3", 1);

        var accountOneRoles = new ArrayList<Role>();
        accountOneRoles.add(new Role(AccountRoleEnum.ROLE_ADMIN));
        accountOne.setRoles(accountOneRoles);
        var getAdminsResult = new ArrayList<Account>();
        getAdminsResult.add(accountOne);
        Mockito.when(accountMokRepository.findAccountByRolesContains(accountOne.getRoles().getFirst())).thenReturn(getAdminsResult);


        var accountTwoRoles = new ArrayList<Role>();
        accountTwoRoles.add(new Role(AccountRoleEnum.ROLE_PARTICIPANT));
        accountTwo.setRoles(accountTwoRoles);
        var getParticipantResult = new ArrayList<Account>();
        getParticipantResult.add(accountTwo);
        Mockito.when(accountMokRepository.findAccountByRolesContains(accountTwo.getRoles().getFirst())).thenReturn(getParticipantResult);

        var accountThreeRoles = new ArrayList<Role>();
        accountThreeRoles.add(new Role(AccountRoleEnum.ROLE_MANAGER));
        accountThree.setRoles(accountThreeRoles);
        var getManagerResults = new ArrayList<Account>();
        getManagerResults.add(accountThree);
        Mockito.when(accountMokRepository.findAccountByRolesContains(accountThree.getRoles().getFirst())).thenReturn(getManagerResults);

        Mockito.when(roleRepository.findByName(AccountRoleEnum.ROLE_ADMIN)).thenReturn(Optional.of(accountOne.getRoles().getFirst()));
        Mockito.when(roleRepository.findByName(AccountRoleEnum.ROLE_PARTICIPANT)).thenReturn(Optional.of(accountTwo.getRoles().getFirst()));
        Mockito.when(roleRepository.findByName(AccountRoleEnum.ROLE_MANAGER)).thenReturn(Optional.of(accountThree.getRoles().getFirst()));

        List<Account> accountList = new ArrayList<>() {{
            add(accountOne);
            add(accountTwo);
            add(accountThree);
        }};

        Mockito.when(accountMokRepository.findAll()).thenReturn(accountList);
        Mockito.when(accountMokRepository.findById(accountOne.getId())).thenReturn(Optional.of(accountOne));
        Mockito.when(accountMokRepository.saveAndFlush(accountOne)).thenReturn(accountOne);
        Mockito.when(accountMokRepository.findByUsername(accountOne.getUsername())).thenReturn(Optional.of(accountOne));
    }

    @Test
    void testGetAllAccounts() throws Exception {
        var accounts = accountService.getAllAccounts();
        Assertions.assertEquals(accounts.size(), 3);
    }

    @Test
    void testGetAccountById() throws Exception {
        var account = accountService.getAccountById(accountOne.getId());
        Assertions.assertEquals(account, accountOne);
    }

    @Test
    void testGetAdmins() throws Exception {
        var admins = accountService.getAdmins();
        Assertions.assertEquals(admins.size(), 1);
        Assertions.assertEquals(admins.getFirst(), accountOne);
    }

    @Test
    void testGetParticipants() throws Exception {
        var admins = accountService.getParticipants();
        Assertions.assertEquals(admins.size(), 1);
        Assertions.assertEquals(admins.getFirst(), accountTwo);
    }

    @Test
    void testGetManagers() throws Exception {
        var admins = accountService.getManagers();
        Assertions.assertEquals(admins.size(), 1);
        Assertions.assertEquals(admins.getFirst(), accountThree);
    }

    @Test
    void testSetAccountStatus() throws Exception {
        var account = accountService.setAccountStatus(accountOne.getId(), true);
        Assertions.assertTrue(account.getActive());
        account = accountService.setAccountStatus(accountOne.getId(), false);
        Assertions.assertFalse(account.getActive());
    }

    @Test
    void testGetAccountByUsername() throws Exception {
        var account = accountService.getAccountByUsername(accountOne.getUsername());
        Assertions.assertEquals(account, accountOne);
    }

}
