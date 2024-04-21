package pl.lodz.p.it.ssbd2024.ssbd01;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.JwtService;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.messages.ExceptionMessages;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.controller.AccountController;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringJUnitWebConfig(classes = {WebConfig.class})
public class AccountControllerTest {

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMokRepository accountMokRepository;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    private MockMvc mockMvcAccount;

    @Autowired
    private Filter springSecurityFilterChain;

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
        this.mockMvcAccount = MockMvcBuilders
                .standaloneSetup(accountController)
                .addFilter(springSecurityFilterChain)
                .setHandlerExceptionResolvers(handlerExceptionResolver)
                .build();
    }

    @Test
    public void testGetAllAccountsEndpoint() throws Exception {
        Account admin =
                new Account("admingeteall", passwordEncoder.encode("password"), "emaiadmingetall2b@email.com", 0, "firstName11", "lastName11");
        admin = accountService.addAccount(admin);
        accountService.addRoleToAccount(admin.getId(), "ADMIN");
        String adminToken = jwtService.generateToken(admin);

        Account account = new Account("user3", passwordEncoder.encode("password"), "email3@email.com", 0, "firstName3", "lastName3");
        accountService.addAccount(account);

        MvcResult result = mockMvcAccount.perform(get("/api/accounts")
                        .header("Authorization", "Bearer " + adminToken))
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
    public void testAddRoleToAccountEndpoint() throws Exception {
        Account admin =
                new Account("adminaddrole", passwordEncoder.encode("password"), "emaiaadminarole2b@email.com", 0, "firstName11", "lastName11");
        admin = accountService.addAccount(admin);
        accountService.addRoleToAccount(admin.getId(), "ADMIN");
        String adminToken = jwtService.generateToken(admin);
        Account account = new Account("user4", passwordEncoder.encode("password"), "email4test@email.com", 0, "firstName4", "lastName4");
        accountService.addAccount(account);

        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "ADMIN"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveRoleFromAccountEndpoint() throws Exception {
        Account admin =
                new Account("adminremoverole", passwordEncoder.encode("password"), "emaiaremoverole2b@email.com", 0, "firstName11", "lastName11");
        admin = accountService.addAccount(admin);
        accountService.addRoleToAccount(admin.getId(), "ADMIN");
        String adminToken = jwtService.generateToken(admin);
        Account account = new Account("user5", passwordEncoder.encode("password"), "email5@email.com", 1, "firstName5", "lastName5");
        accountService.addAccount(account);
        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "ADMIN"))
                .andExpect(status().isOk());


        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "PARTICIPANT"))
                .andExpect(status().isBadRequest());

        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "ADMIN"))
                .andExpect(status().isConflict());

        mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/removeRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "PARTICIPANT"))
                .andExpect(status().isBadRequest());

        mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/removeRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "MANAGER"))
                .andExpect(status().isBadRequest());

        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "MANAGER"))
                .andExpect(status().isOk());

        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "PARTICIPANT"))
                .andExpect(status().isUnprocessableEntity());

        mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/removeRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "ADMIN"))
                .andExpect(status().isOk());

        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "PARTICIPANT"))
                .andExpect(status().isBadRequest());


        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "MANAGER"))
                .andExpect(status().isConflict());

        mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/removeRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "MANAGER"))
                .andExpect(status().isOk());

        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "PARTICIPANT"))
                .andExpect(status().isOk());

        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "PARTICIPANT"))
                .andExpect(status().isConflict());

        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "ADMIN"))
                .andExpect(status().isBadRequest());

        mockMvcAccount.perform(post("/api/accounts/" + account.getId() + "/addRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "MANAGER"))
                .andExpect(status().isBadRequest());

        mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/removeRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "MANAGER"))
                .andExpect(status().isBadRequest());

        mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/removeRole")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("roleName", "ADMIN"))
                .andExpect(status().isBadRequest());


    }

    @Test
    public void testSetActiveAccountEndpoint() throws Exception {

        Account account = new Account("user10", passwordEncoder.encode("password"), "email10@email.com", 0, "firstName10", "lastName10");
        account = accountService.addAccount(account);
        mockMvcAccount.perform(patch("/api/accounts/" + account.getId() + "/setActive"));
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "ADMIN");
        String adminToken = jwtService.generateToken(account);
        mockMvcAccount.perform(patch("/api/accounts/" + account.getId() + "/setActive")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
        mockMvcAccount.perform(patch("/api/accounts/" + UUID.randomUUID() + "/setActive")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(ExceptionMessages.ACCOUNT_NOT_FOUND)));

    }

    @Test
    public void testSetInactiveAccountEndpoint() throws Exception {

        Account account = new Account("user11", passwordEncoder.encode("password"), "email11@email.com", 0, "firstName11", "lastName11");
        account = accountService.addAccount(account);
        mockMvcAccount.perform(patch("/api/accounts/" + account.getId() + "/setInactive"));
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "ADMIN");
        String adminToken = jwtService.generateToken(account);
        mockMvcAccount.perform(patch("/api/accounts/" + account.getId() + "/setInactive")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
        mockMvcAccount.perform(patch("/api/accounts/" + UUID.randomUUID() + "/setInactive")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(ExceptionMessages.ACCOUNT_NOT_FOUND)));

    }

    @Test
    public void testGetAccountByUsernameEndpoint() throws Exception {
        Account account = new Account("user12", passwordEncoder.encode("password"), "email12@email.com", 0, "firstName12", "lastName12");
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "ADMIN");
        String token = jwtService.generateToken(account);

        mockMvcAccount.perform(get("/api/accounts/username/" + account.getUsername())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvcAccount.perform(get("/api/accounts/username/BAD_USERNAME")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    public void testUpdateAccountUserDataEndpoint() throws Exception {

        Account account = new Account("user13", passwordEncoder.encode("password"), "email13@email.com", 0, "firstName13", "lastName13");
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "ADMIN");
        account.setFirstName("newfirstName13");
        String jsonAccount = objectMapper.writeValueAsString(account);
        String adminToken = jwtService.generateToken(account);
        System.out.println(adminToken);
        MvcResult result = mockMvcAccount.perform(put("/api/accounts/userData/" + account.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAccount))
                .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("newfirstName13"));

        mockMvcAccount.perform(put("/api/accounts/userData/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAccount))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(ExceptionMessages.ACCOUNT_NOT_FOUND)));
    }

    @Test
    public void testUpdateAccountUserDataEndpointAsParticipant() throws Exception {

        Account account = new Account("user14", passwordEncoder.encode("password"), "email14@email.com", 0, "firstName14", "lastName14");
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "PARTICIPANT");
        account.setFirstName("newfirstName14");
        String jsonAccount = objectMapper.writeValueAsString(account);
        String token = jwtService.generateToken(account);
        Account finalAccount = account;

        mockMvcAccount.perform(put("/api/accounts/userData/" + finalAccount.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAccount))
                .andExpect(status().isForbidden());

    }

    @Test
    public void testGetParticipants() throws Exception {
        Account account = new Account("participant", passwordEncoder.encode("password"), "email123@email.com", 0, "firstName11", "lastName11");
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "PARTICIPANT");
        Account admin = new Account("admintest6", passwordEncoder.encode("password"), "email11b@email.com", 0, "firstName11", "lastName11");
        admin = accountService.addAccount(admin);
        accountService.addRoleToAccount(admin.getId(), "ADMIN");
        String adminToken = jwtService.generateToken(admin);
        mockMvcAccount.perform(get("/api/accounts/participants")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[-1].username").value(account.getUsername()))
                .andExpect(jsonPath("$[-1].email").value(account.getEmail()))
                .andExpect(jsonPath("$[-1].firstName").value(account.getFirstName()))
                .andExpect(jsonPath("$[-1].lastName").value(account.getLastName()))
                .andExpect(jsonPath("$[-1].email").value(account.getEmail()));
    }

    @Test
    public void testGetParticipantsNotFound() throws Exception {
        accountMokRepository.deleteAll();
        Account admin =
                new Account("admintestnotfoud", passwordEncoder.encode("password"), "email11notfoundb@email.com", 0, "firstName11", "lastName11");
        admin = accountService.addAccount(admin);
        accountService.addRoleToAccount(admin.getId(), "ADMIN");
        String adminToken = jwtService.generateToken(admin);
        mockMvcAccount.perform(get("/api/accounts/participants")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(ExceptionMessages.NO_PARTICIPANTS_FOUND)));


    }

    @Test
    public void testGetParticipantsUnauthorized() throws Exception {
        Account account = new Account("participantNOTAUTHORIZED", passwordEncoder.encode("password"), "email1NOTAUTH2@email.com", 0, "firstName11",
                "lastName11");
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "PARTICIPANT");
        mockMvcAccount.perform(get("/api/accounts/participants"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAdministrators() throws Exception {
        Account admin = new Account("admin2", passwordEncoder.encode("password"), "emaiadmin2b@email.com", 0, "firstName11", "lastName11");
        admin = accountService.addAccount(admin);
        accountService.addRoleToAccount(admin.getId(), "ADMIN");
        String adminToken = jwtService.generateToken(admin);
        mockMvcAccount.perform(get("/api/accounts/administrators")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[-1].username").value(admin.getUsername()))
                .andExpect(jsonPath("$[-1].email").value(admin.getEmail()))
                .andExpect(jsonPath("$[-1].firstName").value(admin.getFirstName()))
                .andExpect(jsonPath("$[-1].lastName").value(admin.getLastName()))
                .andExpect(jsonPath("$[-1].email").value(admin.getEmail()));
    }

    @Test
    public void testGetManagers() throws Exception {
        Account account = new Account("manager", passwordEncoder.encode("password"), "email4@email.com", 0, "firstName11", "lastName11");
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "MANAGER");
        Account admin = new Account("admi3", passwordEncoder.encode("password"), "emailadmin3@email.com", 0, "firstName11", "lastName11");
        admin = accountService.addAccount(admin);
        accountService.addRoleToAccount(admin.getId(), "ADMIN");
        String adminToken = jwtService.generateToken(admin);
        mockMvcAccount.perform(get("/api/accounts/managers")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[-1].username").value(account.getUsername()))
                .andExpect(jsonPath("$[-1].email").value(account.getEmail()))
                .andExpect(jsonPath("$[-1].firstName").value(account.getFirstName()))
                .andExpect(jsonPath("$[-1].lastName").value(account.getLastName()))
                .andExpect(jsonPath("$[-1].email").value(account.getEmail()));
    }

    @Test
    public void testGetManagersNotFound() throws Exception {
        accountMokRepository.deleteAll();
        Account admin =
                new Account("adminManNotFound", passwordEncoder.encode("password"), "emaiadminNotFOunds2b@email.com", 0, "firstName11", "lastName11");
        admin = accountService.addAccount(admin);
        accountService.addRoleToAccount(admin.getId(), "ADMIN");
        String adminToken = jwtService.generateToken(admin);
        mockMvcAccount.perform(get("/api/accounts/managers")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(ExceptionMessages.NO_MANAGERS_FOUND)));

    }

    @Test
    public void testUpdateAccountEmail() throws Exception {
        Account account = new Account("user15", passwordEncoder.encode("password"), "email5@email.com", 0, "firstName15", "lastName15");
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "ADMIN");
        String newEmail = objectMapper.writeValueAsString(new JSONObject().appendField("email", "newemail@email.com"));
        String adminToken = jwtService.generateToken(account);
        mockMvcAccount.perform(patch("/api/accounts/email/" + account.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newemail@email.com"));
        Assertions.assertThrows(AssertionError.class, () -> {
            mockMvcAccount.perform(patch("/api/accounts/email/" + "BAD_ID")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newEmail))
                    .andExpect(status().isOk());
        });
    }

    @Test
    public void testUpdateAccountEmailAsParticipant() throws Exception {
        Account account = new Account("user16", passwordEncoder.encode("password"), "email16@email.com", 0, "firstName16", "lastName16");
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "PARTICIPANT");
        String newEmail = objectMapper.writeValueAsString(new JSONObject().appendField("email", "newemail16@email.com"));
        String token = jwtService.generateToken(account);
        mockMvcAccount.perform(patch("/api/accounts/email/" + account.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEmail))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateAccountPassword() throws Exception {
        Account account = new Account("user20", passwordEncoder.encode("password"), "email20@email.com", 0, "firstName15", "lastName15");
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "MANAGER");
        String newPassword = objectMapper.writeValueAsString(new JSONObject().appendField("value", "newpassword"));
        String token = jwtService.generateToken(account);
        mockMvcAccount.perform(patch("/api/accounts/mypassword/" + account.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPassword))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateAccountPasswordUnauthorized() throws Exception {
        Account account = new Account("user21", passwordEncoder.encode("password"), "email21@email.com", 0, "firstName15", "lastName15");
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "MANAGER");
        String newPassword = objectMapper.writeValueAsString(new JSONObject().appendField("value", "newpassword"));
        mockMvcAccount.perform(patch("/api/accounts/mypassword/" + account.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPassword))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateOtherAccountPassword() throws Exception {
        Account account = new Account("user22", passwordEncoder.encode("password"), "email22@email.com", 0, "firstName15", "lastName15");
        account = accountService.addAccount(account);
        accountService.addRoleToAccount(account.getId(), "MANAGER");

        String newPassword = objectMapper.writeValueAsString(new JSONObject().appendField("value", "newpassword"));

        Account accountParticipant = new Account("user23", passwordEncoder.encode("password"), "email23@email.com", 0, "firstName15", "lastName15");
        accountParticipant = accountService.addAccount(accountParticipant);
        accountService.addRoleToAccount(accountParticipant.getId(), "PARTICIPANT");

        String token = jwtService.generateToken(accountParticipant);

        mockMvcAccount.perform(patch("/api/accounts/mypassword/" + account.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPassword))
                .andExpect(status().isForbidden());
    }
}