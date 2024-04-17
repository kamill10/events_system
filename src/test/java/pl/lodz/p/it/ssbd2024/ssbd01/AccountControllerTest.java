package pl.lodz.p.it.ssbd2024.ssbd01;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
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
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.controllers.AccountController;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.services.AccountService;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
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
            mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/removeRole")
                            .param("roleName", "PARTICIPANT"))
                    .andExpect(status().isOk());
        });

        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/removeRole")
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

        mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/removeRole")
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

        mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/removeRole")
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
            mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/removeRole")
                            .param("roleName", "MANAGER"))
                    .andExpect(status().isOk());
        });

        Assertions.assertThrows(Exception.class, () -> {
            mockMvcAccount.perform(delete("/api/accounts/" + account.getId() + "/removeRole")
                            .param("roleName", "ADMIN"))
                    .andExpect(status().isOk());
        });


    }

    @Test
    public void testSetActiveAccountEndpoint() throws Exception {

        Account account = new Account("user10", passwordEncoder.encode("password"), "email10@email.com", 0, "firstName10", "lastName10");
        account = accountService.addUser(account);
        accountService.addRoleToAccount(account.getId(), "ADMIN");
        String adminToken = jwtService.generateToken(account);
        mockMvcAccount.perform(patch("/api/accounts/" + account.getId() + "/setActive")
                        .header("Authorization", "Bearer " + adminToken))
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
        accountService.addRoleToAccount(account.getId(), "ADMIN");
        String adminToken = jwtService.generateToken(account);
        mockMvcAccount.perform(patch("/api/accounts/" + account.getId() + "/setInactive")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
        Assertions.assertThrows(AssertionError.class, () -> {
            mockMvcAccount.perform(patch("/api/accounts/" + "BAD_ID" + "/setInactive"))
                    .andExpect(status().isOk());
        });

    }

    @Test
    public void testGetAccountByUsernameEndpoint() throws Exception {
        Account account = new Account("user12", passwordEncoder.encode("password"), "email12@email.com", 0, "firstName12", "lastName12");
        account = accountService.addUser(account);

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
        account = accountService.addUser(account);
        accountService.addRoleToAccount(account.getId(), "ADMIN");
        account.setEmail("newemail13@email.com");
        String jsonAccount = objectMapper.writeValueAsString(account);
        String adminToken = jwtService.generateToken(account);
        System.out.println(adminToken);
        MvcResult result = mockMvcAccount.perform(put("/api/accounts/userData/" + account.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAccount))
                .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("newemail13"));

        Assertions.assertThrows(AssertionError.class, () -> {
            mockMvcAccount.perform(put("/api/accounts/userData/" + "BAD_ID")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonAccount))
                    .andExpect(status().isOk());
        });
    }

    @Test
    public void testUpdateAccountUserDataEndpointAsParticipant() throws Exception {

        Account account = new Account("user14", passwordEncoder.encode("password"), "email14@email.com", 0, "firstName14", "lastName14");
        account = accountService.addUser(account);
        accountService.addRoleToAccount(account.getId(), "PARTICIPANT");
        account.setEmail("newemail14@email.com");
        String jsonAccount = objectMapper.writeValueAsString(account);
        String token = jwtService.generateToken(account);
        Account finalAccount = account;

        mockMvcAccount.perform(put("/api/accounts/userData/" + finalAccount.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAccount))
                .andExpect(status().isForbidden());

    }
}

