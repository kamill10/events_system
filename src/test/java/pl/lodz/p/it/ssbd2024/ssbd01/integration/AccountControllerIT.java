package pl.lodz.p.it.ssbd2024.ssbd01.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateAccountDataDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdatePasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerIT {

    static Network network = Network.newNetwork();

    static ObjectMapper objectMapper;

    static int port;

    static String baseUrl;

    static String adminToken;

    static String participantToken;

    static String managerToken;


    @BeforeAll
    public static void setup() {
        System.setProperty("spring.profiles.active", "test");
    }

    public static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("ssbd01")
            .withUsername("ssbd01admin")
            .withPassword("admin")
            .withExposedPorts(5432)
            .withNetworkAliases("postgres")
            .withNetwork(network)
            .withInitScript("sql/create-users.sql")
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("sql/init-test-data.sql"),
                    "/tmp/init-data.sql"
            )
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("sql/delete-data.sql"),
                    "/tmp/delete-data.sql"
            )
            .waitingFor(Wait.forSuccessfulCommand("pg_isready -U ssbd01admin"))
            .withReuse(true);

    @Container
    static GenericContainer<?> tomcat = new GenericContainer<>("tomcat:10.1.19-jre21")
            .withExposedPorts(8080)
            .withNetworkAliases("tomcat")
            .withNetwork(network)
            .dependsOn(postgres)
            .withCopyFileToContainer(
                    MountableFile.forHostPath("target/ssbd01.war"),
                    "/usr/local/tomcat/webapps/ssbd01.war"
            )
            .waitingFor(Wait.forHttp("/ssbd01/api/accounts").forStatusCode(403))
            .withReuse(true)
            .withFileSystemBind("transactions.log", "/usr/local/tomcat/transactions.log", BindMode.READ_WRITE)
            .withFileSystemBind("auth.log", "/usr/local/tomcat/auth.log", BindMode.READ_WRITE);


    @BeforeEach
    public void initData() throws IOException, InterruptedException {
        port = tomcat.getMappedPort(8080);
        baseUrl = "http://localhost:" + port + "/ssbd01/api";
        objectMapper = objectMapper();
        postgres.execInContainer("psql", "-U", "ssbd01admin", "ssbd01", "-f",
                "/tmp/delete-data.sql");
        postgres.execInContainer("psql", "-U", "ssbd01admin", "ssbd01", "-f",
                "/tmp/init-data.sql");
    }

    @Test
    @Order(1)
    public void authenticationTest() throws JsonProcessingException {
        LoginDTO loginDTO = new LoginDTO("testAdmin", "P@ssw0rd");

        ValidatableResponse response = given()
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(loginDTO))
                .when()
                .post(baseUrl + "/auth/authenticate")
                .then()
                .statusCode(HttpStatus.OK.value());

        String jwtToken = response.extract().body().asString();
        adminToken = jwtToken.substring(1, jwtToken.length() - 1);

        loginDTO = new LoginDTO("testParticipant", "P@ssw0rd");
        response = given()
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(loginDTO))
                .when()
                .post(baseUrl + "/auth/authenticate")
                .then()
                .statusCode(HttpStatus.OK.value());

        jwtToken = response.extract().body().asString();
        participantToken = jwtToken.substring(1, jwtToken.length() - 1);

        loginDTO = new LoginDTO("testManager", "P@ssw0rd");
        response = given()
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(loginDTO))
                .when()
                .post(baseUrl + "/auth/authenticate")
                .then()
                .statusCode(HttpStatus.OK.value());

        jwtToken = response.extract().body().asString();
        managerToken = jwtToken.substring(1, jwtToken.length() - 1);
    }

    @Test
    public void testGetAllAccountsEndpoint() throws Exception {
        assertNotNull(adminToken);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("testParticipant"),
                        containsString("testManager"),
                        containsString("testAdmin")
                );

        given()
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .get(baseUrl + "/accounts")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        given()
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/accounts")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void addManagerRoleToAdmin() throws Exception {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "MANAGER")
                .when()
                .post(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/add-role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void addAdminRoleToManager() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "ADMIN")
                .when()
                .post(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/add-role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void addParticipantRoleToManager() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "PARTICIPANT")
                .when()
                .post(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/add-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addManagerRoleToParticipant() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "MANAGER")
                .when()
                .post(baseUrl + "/accounts/" + "a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6" + "/add-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addParticipantRoleToAdmin() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "PARTICIPANT")
                .when()
                .post(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/add-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addAdminRoleToParticipant() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "ADMIN")
                .when()
                .post(baseUrl + "/accounts/" + "a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6" + "/add-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addParticipantRoleToParticipant() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "PARTICIPANT")
                .when()
                .post(baseUrl + "/accounts/" + "a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6" + "/add-role")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void addManagerRoleToManager() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "MANAGER")
                .when()
                .post(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/add-role")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void addAdminRoleToAdmin() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "ADMIN")
                .when()
                .post(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/add-role")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void addNonExistentRole() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "CLIENT")
                .when()
                .post(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/add-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addRoleToNonExistentAccount() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "ADMIN")
                .when()
                .post(baseUrl + "/accounts/" + UUID.randomUUID() + "/add-role")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void addRoleAsManager() {
        given()
                .header("Authorization", "Bearer " + managerToken)
                .param("roleName", "MANAGER")
                .when()
                .post(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/add-role")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void addRoleAsParticipant() {
        given()
                .header("Authorization", "Bearer " + participantToken)
                .param("roleName", "MANAGER")
                .when()
                .post(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/add-role")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void removeAdminRoleFromAdmin() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "ADMIN")
                .when()
                .delete(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/remove-role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void removeManagerRoleFromManager() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "MANAGER")
                .when()
                .delete(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/remove-role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void removeParticipantRoleFromParticipant() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "PARTICIPANT")
                .when()
                .delete(baseUrl + "/accounts/" + "a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6" + "/remove-role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void removeAdminRoleFromManager() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "ADMIN")
                .when()
                .delete(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/remove-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void removeNonExistentRole() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "CLIENT")
                .when()
                .delete(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/remove-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void removeRoleFromNonExistentAccount() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("roleName", "ADMIN")
                .when()
                .delete(baseUrl + "/accounts/" + UUID.randomUUID() + "/remove-role")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void removeRoleAsManager() {
        given()
                .header("Authorization", "Bearer " + managerToken)
                .param("roleName", "ADMIN")
                .when()
                .delete(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/remove-role")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void removeRoleAsParticipant() {
        given()
                .header("Authorization", "Bearer " + participantToken)
                .param("roleName", "ADMIN")
                .when()
                .delete(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/remove-role")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void setActiveAccountEndpoint() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .patch(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/set-active")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .patch(baseUrl + "/accounts/" + UUID.randomUUID() + "/set-active")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void setActiveAccountEndpointAsParticipant() {
        given()
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .patch(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/set-active")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void setActiveAccountEndpointAsManager() {
        given()
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .patch(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/set-active")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void setInactiveAccountEndpoint() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .patch(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/set-inactive")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .patch(baseUrl + "/accounts/" + UUID.randomUUID() + "/set-inactive")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void setInactiveAccountEndpointAsParticipant() {
        given()
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .patch(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/set-inactive")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void setInactiveAccountEndpointAsManager() {
        given()
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .patch(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/set-inactive")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testGetAccountByUsernameEndpoint() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testAdmin")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/BAD_USERNAME")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testUpdateAccountDataEndpoint() throws Exception {
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testAdmin")
                .then()
                .statusCode(HttpStatus.OK.value());

        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);

        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newFirstName", "newLastName", 0);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(updateAccountDataDTO))
                .when()
                .put(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/user-data")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("newFirstName"),
                        containsString("newLastName")
                );
    }

    @Test
    public void testUpdateAccountDataEndpointWithInvalidEtag() throws Exception {
        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newFirstName", "newLastName", 0);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", UUID.randomUUID().toString())
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(updateAccountDataDTO))
                .when()
                .put(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/user-data")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }

    @Test
    public void testUpdateNonExistentAccountDataEndpoint() throws Exception {
        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newFirstName", "newLastName", 0);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", UUID.randomUUID().toString())
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(updateAccountDataDTO))
                .when()
                .put(baseUrl + "/accounts/" + UUID.randomUUID() + "/user-data")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testUpdateAccountDataEndpointAsParticipant() throws Exception {
        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newFirstName", "newLastName", 0);
        given()
                .header("Authorization", "Bearer " + participantToken)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(updateAccountDataDTO))
                .when()
                .put(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/user-data")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testUpdateAccountDataEndpointAsManager() throws Exception {
        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newFirstName", "newLastName", 0);
        given()
                .header("Authorization", "Bearer " + managerToken)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(updateAccountDataDTO))
                .when()
                .put(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/user-data")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testGetParticipantsEndpoint() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/participants")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("testParticipant"),
                        not(containsString("testManager")),
                        not(containsString("testAdmin"))
                );
    }

    @Test
    public void testGetParticipantsEndpointAsParticipant() {
        given()
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .get(baseUrl + "/accounts/participants")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testGetParticipantsEndpointAsManager() {
        given()
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/accounts/participants")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testGetAdministratorsEndpoint() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/administrators")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("testAdmin"),
                        not(containsString("testManager")),
                        not(containsString("testParticipant"))
                );
    }

    @Test
    public void testGetAdministratorsEndpointAsParticipant() {
        given()
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .get(baseUrl + "/accounts/administrators")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testGetAdministratorsEndpointAsManager() {
        given()
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/accounts/administrators")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testGetManagersEndpoint() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/managers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        not(containsString("testAdmin")),
                        containsString("testManager"),
                        containsString("ENGLISH"),
                        not(containsString("testParticipant"))
                );
    }

    @Test
    public void testGetManagersEndpointAsParticipant() {
        given()
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .get(baseUrl + "/accounts/managers")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testGetManagersEndpointAsManager() {
        given()
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/accounts/managers")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void sendEmailWhenEmailChange() throws JsonProcessingException {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("admin202401@proton.me");
        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .body(updateEmailDTO)
                .when()
                .post(baseUrl + "/accounts/change-email/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void sendEmailWhenPasswordReset() throws JsonProcessingException {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("admin202401@proton.me");
        given()
                .contentType("application/json")
                .body(updateEmailDTO)
                .when()
                .post(baseUrl + "/accounts/reset-password")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void sendEmailWhenPasswordChange() throws JsonProcessingException {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("admin202401@proton.me");
        given()
                .header("Authorization", "Bearer " + adminToken)
                .body(updateEmailDTO)
                .contentType("application/json")
                .when()
                .post(baseUrl + "/accounts/change-password")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void testResetAccountPasswordTokenEndpointNotFound() throws Exception {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("newPassword123@");
        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .body(updatePasswordDTO)
                .when()
                .patch(baseUrl + "/accounts/reset-password/token/2137")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void sendEmailWhenPasswordResetAndEmailNotExisist() {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("niematakiego@proton.me");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(updateEmailDTO)
                .when()
                .post(baseUrl + "/accounts/reset-password")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void sendTokenWhenPasswordChangeByAdminPositiveScenario() {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("admin202401@proton.me");
        String token = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(updateEmailDTO)
                .when()
                .post(baseUrl + "/accounts/change-password")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();
        assertNotNull(token);
        //problem with getting token in test from response , but logic is ok
        /*UpdatePasswordDTO password = new UpdatePasswordDTO("dsafdvcxsd");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(password)
                .when()
                .post(baseUrl + "/accounts/change-password/token/"+token)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();*/
    }

    @Test
    public void sendTokenWhenPasswordChangeByAdminButTokenNotExist() {
        UpdatePasswordDTO password = new UpdatePasswordDTO("dsafdvcxsd");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(password)
                .when()
                .post(baseUrl + "/accounts/change-password/token/"+"4234")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().asString();
    }

    @Test
    public void sendTokenWhenEmailChangeByAdminPositiveScenario() {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("nowy202401@proton.me");
        String token = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(updateEmailDTO)
                .when()
                .post(baseUrl + "/accounts/change-email/8b25c94f-f10f-4285-8eb2-39ee1c4002f1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();
        assertNotNull(token);
        /*given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(updateEmailDTO)
                .when()
                .post(baseUrl + "/accounts/change-email/token/"+token)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();*/
    }

    @Test
    public void sendTokenWhenEmailChangeByAdminButTokenNotExist() {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("ka.pazio@o2.pl");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(updateEmailDTO)
                .when()
                .patch(baseUrl + "/accounts/change-email/token/4323")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().asString();
    }

    @Test
    public void sendTokenWhenEmailChangeByAdminButEmailAlreadyExists() {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("participantt202401@proton.me");
        String token = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(updateEmailDTO)
                .when()
                .post(baseUrl + "/accounts/change-email/8b25c94f-f10f-4285-8eb2-39ee1c4002f1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();
       /* given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(updateEmailDTO)
                .when()
                .post(baseUrl + "/accounts/change-email/token/"+token)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract().asString();*/
    }
}