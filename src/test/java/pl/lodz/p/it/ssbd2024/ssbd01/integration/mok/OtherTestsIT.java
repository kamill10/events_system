package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class OtherTestsIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToAdminTest();
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void testGetAllAccountsEndpoint() {
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
                        containsString(AccountRoleEnum.ROLE_MANAGER.toString()),
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
    public void switchRoleWhichAccountDoesNotHave() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .param("role", AccountRoleEnum.ROLE_MANAGER.toString())
                .when()
                .post(baseUrl + "/me/switch-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void refreshTokenTest() {
        var resp = given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .when()
                .post(baseUrl + "/auth/refresh-token")
                .then()
                .statusCode(HttpStatus.OK.value());
        String jwtToken = resp.extract().body().asString();
        adminToken = jwtToken.substring(1, jwtToken.length() - 1);
    }
}