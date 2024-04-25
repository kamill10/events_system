package pl.lodz.p.it.ssbd2024.ssbd01.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
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

import java.io.IOException;

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
            .withReuse(true);


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
    public void testUpdateMyEmail() throws JsonProcessingException {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("newemail@ssbd.pl");
        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(updateEmailDTO))
                .when()
                .patch(baseUrl + "/me/email")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("newemail@ssbd.pl")
                );
    }

    @Test
    public void testUpdateMyEmailUnAuthorized() throws JsonProcessingException {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("newemail@ssbd.pl");
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
        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newName", "newSurname", 1);
        given()
                .header("Authorization", "Bearer " + adminToken)
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
    public void testUpdateMyAccountDataUnAuthorized() {
        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newName", "newSurname", 1);
        given()
                .contentType("application/json")
                .body(updateAccountDataDTO)
                .when()
                .put(baseUrl + "/me/user-data")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
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
    }

}
