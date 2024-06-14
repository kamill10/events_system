package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdateAccountDataDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOK13changeAnotherAccountPersonalDataIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToAdminTest();
        authenticationToParticipantTest();
        authenticationToManagerTest();
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

        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newFirstName", "newLastName", 0, "Europe/Warsaw", "Light");
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
        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newFirstName", "newLastName", 0, "Europe/Warsaw", "Light");
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
        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newFirstName", "newLastName", 0, "Europe/Warsaw", "Light");
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
        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newFirstName", "newLastName", 0, "Europe/Warsaw", "Light");
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
        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newFirstName", "newLastName", 0, "Europe/Warsaw", "Light");
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
    public void testUpdateAccountDataAndAddToHistory() throws Exception {
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/accounts/username/testAdmin")
                .then()
                .statusCode(HttpStatus.OK.value());

        String eTag = response.extract().header("ETag").substring(1, response.extract().header("ETag").length() - 1);

        UpdateAccountDataDTO updateAccountDataDTO = new UpdateAccountDataDTO("newFirstName", "newLastName", 0, "Europe/Warsaw", "Light");
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
        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .when()
                .get(baseUrl + "/accounts/history/testAdmin")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("newFirstName"),
                        containsString("newLastName")
                );
    }
}
