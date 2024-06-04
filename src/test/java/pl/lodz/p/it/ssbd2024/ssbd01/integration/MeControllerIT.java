package pl.lodz.p.it.ssbd2024.ssbd01.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdateAccountDataDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdateEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;

import java.io.IOException;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MeControllerIT {

    static Network network = Network.newNetwork();

    static ObjectMapper objectMapper;

    static int port;

    static String baseUrl;

    static String adminToken;

    static String participantToken;

    static String managerToken;


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
            .withFileSystemBind("auth.log", "/usr/local/tomcat/auth.log", BindMode.READ_WRITE)
            .withFileSystemBind("switch_role.log", "/usr/local/tomcat/switch_role.log", BindMode.READ_WRITE)
            .withFileSystemBind("all_method.log", "/usr/local/tomcat/all_method.log", BindMode.READ_WRITE);


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
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
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
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
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
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .body(objectMapper.writeValueAsString(loginDTO))
                .when()
                .post(baseUrl + "/auth/authenticate")
                .then()
                .statusCode(HttpStatus.OK.value());

        jwtToken = response.extract().body().asString();
        managerToken = jwtToken.substring(1, jwtToken.length() - 1);
    }

    @Test
    public void testGetMyAccount() {
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + participantToken)
                .contentType("application/json")
                .when()
                .get(baseUrl + "/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("testParticipant")
                );
    }

    @Test
    public void testUpdateMyEmailUnAuthorized() throws JsonProcessingException {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("ssbd01@proton.me");
        given()
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(updateEmailDTO))
                .when()
                .patch(baseUrl + "/me/email")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testUpdateMyAccountData() {
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .when()
                .get(baseUrl + "/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("testAdmin")
                );
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);

        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newName", "newSurname", 1, "Europe/Warsaw", "Light");
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .contentType("application/json")
                .body(updateAccountDataDTO)
                .when()
                .put(baseUrl + "/me/user-data")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("newName"),
                        containsString("newSurname"),
                        containsString("1")
                );
    }

    @Test
    public void testUpdateMyAccountDataWithInvalidETag() {
        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newName", "newSurname", 1, "Europe/Warsaw", "Light");
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", UUID.randomUUID().toString())
                .contentType("application/json")
                .body(updateAccountDataDTO)
                .when()
                .put(baseUrl + "/me/user-data")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }

    @Test
    public void testUpdateMyAccountDataUnAuthorized() {
        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newName", "newSurname", 1, "Europe/Warsaw", "Light");
        given()
                .contentType("application/json")
                .body(updateAccountDataDTO)
                .when()
                .put(baseUrl + "/me/user-data")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    /*@Test
    public void testUpdateMyPassword() {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("newPassword");

        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .body(updatePasswordDTO)
                .when()
                .patch(baseUrl + "/me/password")
                .then()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    public void testUpdateMyPasswordUnAuthorized() {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("newPassword");

        given()
                .contentType("application/json")
                .body(updatePasswordDTO)
                .when()
                .patch(baseUrl + "/me/password")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    } */
    @Test
    public void switchRoleAndLog() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("role", AccountRoleEnum.ROLE_ADMIN.toString())
                .when()
                .post(baseUrl + "/me/switch-role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void switchRoleWhichAccountDoesNotHave(){
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("role", AccountRoleEnum.ROLE_MANAGER.toString())
                .when()
                .post(baseUrl + "/me/switch-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

}