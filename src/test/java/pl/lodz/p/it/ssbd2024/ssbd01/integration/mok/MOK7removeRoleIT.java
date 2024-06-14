package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class MOK7removeRoleIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToAdminTest();
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void removeAdminRoleFromAdmin() {
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
                .delete(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/remove-role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void removeManagerRoleFromManager() {
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
                .delete(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/remove-role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void removeParticipantRoleFromParticipant() {
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
                .delete(baseUrl + "/accounts/" + "a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6" + "/remove-role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void removeAdminRoleFromManager() {
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
                .delete(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/remove-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void removeNonExistentRole() {
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
                .param("roleName", "CLIENT")
                .when()
                .delete(baseUrl + "/accounts/" + "5454d58c-6ae2-4eee-8980-a49a1664f157" + "/remove-role")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void removeRoleFromNonExistentAccount() {
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
                .delete(baseUrl + "/accounts/" + UUID.randomUUID() + "/remove-role")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void removeRoleAsManager() {
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
                .param("roleName", AccountRoleEnum.ROLE_ADMIN.toString())
                .when()
                .delete(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/remove-role")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void removeRoleAsParticipant() {
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
                .param("roleName", AccountRoleEnum.ROLE_ADMIN.toString())
                .when()
                .delete(baseUrl + "/accounts/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1" + "/remove-role")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
