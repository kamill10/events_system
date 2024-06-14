package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOW24createRoomIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void createRoomPositiveTest() {
        var createRoom = new CreateRoomDTO(
                "Room 1",
                UUID.fromString("da7cfcfa-5f1c-4a85-8f93-1022f28f747a"),
                50
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .body(createRoom)
                .when()
                .post(baseUrl + "/rooms/room")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void createRoomNegativeCapacityTest() {
        var createRoom = new CreateRoomDTO(
                "Room 1",
                UUID.fromString("da7cfcfa-5f1c-4a85-8f93-1022f28f747a"),
                -1
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .body(createRoom)
                .when()
                .post(baseUrl + "/rooms/room")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void createRoomNameTooShortTest() {
        var createRoom = new CreateRoomDTO(
                "R",
                UUID.fromString("da7cfcfa-5f1c-4a85-8f93-1022f28f747a"),
                50
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .body(createRoom)
                .when()
                .post(baseUrl + "/rooms/room")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void createRoomNameAlreadyExistsTest() {
        var createRoom = new CreateRoomDTO(
                "443",
                UUID.fromString("da7cfcfa-5f1c-4a85-8f93-1022f28f747a"),
                50
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .body(createRoom)
                .when()
                .post(baseUrl + "/rooms/room")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void createRoomNegativeByParticipant() {
        var createRoom = new CreateRoomDTO(
                "Room 1",
                UUID.fromString("da7cfcfa-5f1c-4a85-8f93-1022f28f747a"),
                50
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .header("Accept-Language", "en-US")
                .body(createRoom)
                .when()
                .post(baseUrl + "/rooms/room")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void createRoomNoTokenTest() {
        var createRoom = new CreateRoomDTO(
                "Room 1",
                UUID.fromString("da7cfcfa-5f1c-4a85-8f93-1022f28f747a"),
                50
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "en-US")
                .body(createRoom)
                .when()
                .post(baseUrl + "/rooms/room")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void updateRoomPositiveTest() {
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

        var roomToUpdate = new UpdateRoomDTO(
                "999",
                55
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .header("If-Match", etag)
                .body(roomToUpdate)
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
                        containsString("999")
                );
    }

    @Test
    public void updateRoomWrongRoleTest() {
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

        var roomToUpdate = new UpdateRoomDTO(
                "999",
                55
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .header("Accept-Language", "en-US")
                .header("If-Match", etag)
                .body(roomToUpdate)
                .when()
                .patch(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void updateRoomNameTooShortTest() {
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

        var roomToUpdate = new UpdateRoomDTO(
                "9",
                55
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .header("If-Match", etag)
                .body(roomToUpdate)
                .when()
                .patch(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateRoomNegativeCapacityTest() {
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

        var roomToUpdate = new UpdateRoomDTO(
                "999",
                -1
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .header("If-Match", etag)
                .body(roomToUpdate)
                .when()
                .patch(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateRoomNameAlreadyExists() {
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

        var roomToUpdate = new UpdateRoomDTO(
                "Room 1",
                55
        );

        var createRoom = new CreateRoomDTO(
                "Room 1",
                UUID.fromString("da7cfcfa-5f1c-4a85-8f93-1022f28f747a"),
                50
        );

        var addedRoom = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .body(createRoom)
                .when()
                .post(baseUrl + "/rooms/room")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .header("If-Match", etag)
                .body(roomToUpdate)
                .when()
                .patch(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }
}
