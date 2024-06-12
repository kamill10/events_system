package pl.lodz.p.it.ssbd2024.ssbd01.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdateAccountDataDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdateEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;


public class MeControllerIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToAdminTest();
        authenticationToParticipantTest();
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

}