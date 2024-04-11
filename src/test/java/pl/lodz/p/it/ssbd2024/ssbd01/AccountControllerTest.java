package pl.lodz.p.it.ssbd2024.ssbd01;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.controllers.AccountController;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.services.AccountService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = {WebConfig.class})
public class AccountControllerTest {

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountService accountService;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvcAccount;

    @BeforeEach
    void setup() {
        this.mockMvcAccount = MockMvcBuilders
                .standaloneSetup(accountController)
                .setHandlerExceptionResolvers(handlerExceptionResolver)
                .build();
    }

    @Test
    public void testGetAllAccountsEndpoint() throws Exception {

        Account account = new Account("user3", passwordEncoder.encode("password"), "email3@email.com", 0, "firstName3", "lastName3");
        accountService.addUser(account);

        MvcResult result = mockMvcAccount.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains(account.getUsername()));
        Assertions.assertTrue(content.contains(account.getEmail()));
        Assertions.assertTrue(content.contains(account.getFirstName()));
        Assertions.assertTrue(content.contains(account.getLastName()));
        Assertions.assertTrue(content.contains(String.valueOf(account.getGender())));
    }

    @Test
    public void testCreateAccountEndpoint() throws Exception {

        MvcResult result = mockMvcAccount.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user2\",\"password\":\"password\",\"email\":\"email2@email.com\",\"gender\":\"0\"," +
                                "\"firstName\":\"firstName2\",\"lastName\":\"lastName2\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("user2"));
    }

    @Test
    public void testAddRoleToAccountEndpoint() throws Exception {

        Account account = new Account("user4", passwordEncoder.encode("password"), "email4@email.com", 0, "firstName4", "lastName4");
        accountService.addUser(account);

        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .param("roleName", "ADMIN"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveRoleFromAccountEndpoint() throws Exception {

        Account account = new Account("user5", passwordEncoder.encode("password"), "email5@email.com", 1, "firstName5", "lastName5");
        accountService.addUser(account);
        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .param("roleName", "ADMIN"))
                .andExpect(status().isOk());


        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                            .param("roleName", "PARTICIPANT"))
                    .andExpect(status().isOk());
        });

        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                            .param("roleName", "ADMIN"))
                    .andExpect(status().isOk());
        });

        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/takeRole")
                            .param("roleName", "PARTICIPANT"))
                    .andExpect(status().isOk());
        });

        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/takeRole")
                            .param("roleName", "MANAGER"))
                    .andExpect(status().isOk());
        });

        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .param("roleName", "MANAGER"))
                .andExpect(status().isOk());

        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                            .param("roleName", "PARTICIPANT"))
                    .andExpect(status().isOk());
        });

        mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/takeRole")
                        .param("roleName", "ADMIN"))
                .andExpect(status().isOk());

        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                            .param("roleName", "PARTICIPANT"))
                    .andExpect(status().isOk());
        });


        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                            .param("roleName", "MANAGER"))
                    .andExpect(status().isOk());
        });

        mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/takeRole")
                        .param("roleName", "MANAGER"))
                .andExpect(status().isOk());

        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .param("roleName", "PARTICIPANT"))
                .andExpect(status().isOk());

        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                            .param("roleName", "PARTICIPANT"))
                    .andExpect(status().isOk());
        });

        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                            .param("roleName", "ADMIN"))
                    .andExpect(status().isOk());
        });

        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                            .param("roleName", "MANAGER"))
                    .andExpect(status().isOk());
        });

        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/takeRole")
                            .param("roleName", "MANAGER"))
                    .andExpect(status().isOk());
        });

        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/takeRole")
                            .param("roleName", "ADMIN"))
                    .andExpect(status().isOk());
        });


    }
    @Test
    public void testSetActiveAccountEndpoint() throws Exception {

        Account account = new Account("user10", passwordEncoder.encode("password"), "email10@email.com", 0, "firstName10", "lastName10");
        account = accountService.addUser(account);
        mockMvcAccount.perform(patch("/api/accounts/" + account.getId() + "/setActive"))
                .andExpect(status().isOk());
        Assertions.assertThrows(AssertionError.class, () -> {
            mockMvcAccount.perform(patch("/api/accounts/" + "BAD_ID" + "/setActive"))
                    .andExpect(status().isOk());

        });
    }
    @Test
    public void testSetInactiveAccountEndpoint() throws Exception {

        Account account = new Account("user11", passwordEncoder.encode("password"), "email11@email.com", 0, "firstName11", "lastName11");
        account = accountService.addUser(account);
        mockMvcAccount.perform(patch("/api/accounts/" + account.getId() + "/setInactive"))
                .andExpect(status().isOk());
        Assertions.assertThrows(AssertionError.class, () -> {
            mockMvcAccount.perform(patch("/api/accounts/" + "BAD_ID" + "/setInactive"))
                    .andExpect(status().isOk());
        });

    }
}