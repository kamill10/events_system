package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MOK6addRoleIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToAdminTest();
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void addManagerRoleToAdmin() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testAdmin")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .param("roleName", AccountRoleEnum.ROLE_MANAGER.toString())
                .when()
                .post(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/add-role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void addAdminRoleToManager() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testManager")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .param("roleName", AccountRoleEnum.ROLE_ADMIN.toString())
                .when()
                .post(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/add-role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void addParticipantRoleToManager() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testManager")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .param("roleName", AccountRoleEnum.ROLE_PARTICIPANT.toString())
                .when()
                .post(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/add-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addManagerRoleToParticipant() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testParticipant")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .param("roleName", "MANAGER")
                .when()
                .post(baseUrl + "/accounts/" + "a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6" + "/add-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addParticipantRoleToAdmin() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testAdmin")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        var respon = given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .param("roleName", "PARTICIPANT")
                .when()
                .post(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/add-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addAdminRoleToParticipant() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testParticipant")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        var respon = given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .param("roleName", "ADMIN")
                .when()
                .post(baseUrl + "/accounts/" + "a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6" + "/add-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addParticipantRoleToParticipant() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testParticipant")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .param("roleName", AccountRoleEnum.ROLE_PARTICIPANT.toString())
                .when()
                .post(baseUrl + "/accounts/" + "a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6" + "/add-role")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void addManagerRoleToManager() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testManager")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .param("roleName", AccountRoleEnum.ROLE_MANAGER.toString())
                .when()
                .post(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/add-role")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void addAdminRoleToAdmin() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testAdmin")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .param("roleName", AccountRoleEnum.ROLE_ADMIN.toString())
                .when()
                .post(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/add-role")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void addNonExistentRole() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testAdmin")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .param("roleName", "CLIENT")
                .when()
                .post(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/add-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addRoleToNonExistentAccount() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testAdmin")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .param("roleName", AccountRoleEnum.ROLE_ADMIN.toString())
                .when()
                .post(baseUrl + "/accounts/" + UUID.randomUUID() + "/add-role")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void addRoleAsManager() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testAdmin")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        given()
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", eTag)
                .param("roleName", AccountRoleEnum.ROLE_MANAGER.toString())
                .when()
                .post(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/add-role")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void addRoleAsParticipant() {
        var response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testAdmin")
                .then()
                .statusCode(HttpStatus.OK.value());
        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        given()
                .header("Authorization", "Bearer " + participantToken)
                .header("If-Match", eTag)
                .param("roleName", AccountRoleEnum.ROLE_MANAGER.toString())
                .when()
                .post(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/add-role")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testAddRoleToAccountAndCheckHistoryEntry() {
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testManager")
                .then()
                .statusCode(HttpStatus.OK.value());

        var historyResponse = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/history/testManager")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .asString();

        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);
        given()
                .header("Authorization", "Bearer " + adminToken)
                .header("If-Match", eTag)
                .contentType("application/json")
                .when()
                .post(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/add-role?roleName=ROLE_ADMIN")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("ROLE_ADMIN")
                );

        var historyResponseAfter = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/history/testManager")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("testAdmin"),
                        containsString("ROLE_ADMIN")
                )
                .extract()
                .body()
                .asString();
        assertTrue(historyResponseAfter.length() > historyResponse.length());
    }
}
