package pl.lodz.p.it.ssbd2024.ssbd01.mock.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebCoreConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.LanguageEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.RoleNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mock.TestConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.controller.AccountController;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;

import java.lang.reflect.Field;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = {WebCoreConfig.class, TestConfig.class})
class AccountControllerMockTest {


    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountService accountService;

    private MockMvc mockMvcAccount;

    private Account adminAccount;

    private Account participantAccount;

    private Account managerAccount;

    @BeforeEach
    public void setup() throws AccountNotFoundException, NoSuchFieldException, IllegalAccessException, RoleNotFoundException {
        mockMvcAccount = MockMvcBuilders.standaloneSetup(accountController).build();
        adminAccount =
                new Account("admin", "$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y", "admin@email.com", 1, "Jan", "Kowalski",
                        LanguageEnum.POLISH);
        participantAccount =
                new Account("participant", "$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y", "participant@email.com", 1, "Jan",
                        "Kowalski",
                        LanguageEnum.POLISH);
        managerAccount =
                new Account("manager", "$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y", "manager@email.com", 1, "Jan", "Kowalski",
                        LanguageEnum.POLISH);
        List<Role> adminRole = new ArrayList<>() {{
            add(new Role(AccountRoleEnum.ROLE_ADMIN));
        }};
        List<Role> participantRole = new ArrayList<>() {{
            add(new Role(AccountRoleEnum.ROLE_PARTICIPANT));
        }};
        List<Role> managerRole = new ArrayList<>() {{
            add(new Role(AccountRoleEnum.ROLE_MANAGER));
        }};
        adminAccount.setRoles(adminRole);
        participantAccount.setRoles(participantRole);
        managerAccount.setRoles(managerRole);
        Field field1 = AbstractEntity.class.getDeclaredField("version");
        field1.setAccessible(true);
        field1.set(adminAccount, 1L);
        field1.set(participantAccount, 1L);
        field1.set(managerAccount, 1L);
        Mockito.when(accountService.getAccountByUsername(adminAccount.getUsername())).thenReturn(adminAccount);
        Mockito.when(accountService.getAllAccounts()).thenReturn(new ArrayList<>() {{
            add(adminAccount);
        }});
        Mockito.when(accountService.getAdmins()).thenReturn(new ArrayList<>() {{
            add(adminAccount);
        }});
        Mockito.when(accountService.getManagers()).thenReturn(new ArrayList<>() {{
            add(managerAccount);
        }});
        Mockito.when(accountService.getParticipants()).thenReturn(new ArrayList<>() {{
            add(participantAccount);
        }});
    }

    @Test
    void testReadAccountByUsername() throws Exception {
        var accountFromController = mockMvcAccount.perform(get("/api/accounts/username/" + adminAccount.getUsername()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(accountFromController.contains(adminAccount.getUsername()));
    }

    @Test
    void testReadAllAccounts() throws Exception {
        var accountsFromController = mockMvcAccount.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(accountsFromController.contains(adminAccount.getUsername()));
    }

    @Test
    void testReadAllAdmins() throws Exception {
        var adminsFromController = mockMvcAccount.perform(get("/api/accounts/administrators"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(adminsFromController.contains(adminAccount.getUsername()));
    }

    @Test
    void testReadAllManagers() throws Exception {
        var managersFromController = mockMvcAccount.perform(get("/api/accounts/managers"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(managersFromController.contains(managerAccount.getUsername()));
    }

    @Test
    void testReadAllParticipants() throws Exception {
        var participantsFromController = mockMvcAccount.perform(get("/api/accounts/participants"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(participantsFromController.contains(participantAccount.getUsername()));
    }

}
