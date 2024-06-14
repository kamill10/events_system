package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT.*;

public class MOW25editRoomIT extends AbstractControllerIT {
    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void editRoomPositiveTest() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        UpdateRoomDTO updateRoomDTO = new UpdateRoomDTO("nowa",30);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateRoomDTO))
                .when()
                .patch(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value());
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"name\":\"nowa\"")
                );
    }
    @Test
    public void editRoomInValidRoomName() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        UpdateRoomDTO updateRoomDTO = new UpdateRoomDTO("",30);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateRoomDTO))
                .when()
                .patch(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void editRoomNotUniqueName() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        UpdateRoomDTO updateRoomDTO = new UpdateRoomDTO("unique",30);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateRoomDTO))
                .when()
                .patch(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void editRoomInValidCapacity() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        UpdateRoomDTO updateRoomDTO = new UpdateRoomDTO("dobra",3000);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateRoomDTO))
                .when()
                .patch(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
    @Test
    public void editRoomWithoutToken() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        UpdateRoomDTO updateRoomDTO = new UpdateRoomDTO("",30);
        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateRoomDTO))
                .when()
                .patch(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
    @Test
    public void editRoomBadRole() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        UpdateRoomDTO updateRoomDTO = new UpdateRoomDTO("",30);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateRoomDTO))
                .when()
                .patch(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
    @Test
    public void editRoomInvalidEtag() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        UpdateRoomDTO updateRoomDTO = new UpdateRoomDTO("nowa",30);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateRoomDTO))
                .when()
                .patch(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value());
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateRoomDTO))
                .when()
                .patch(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());

}
    }
