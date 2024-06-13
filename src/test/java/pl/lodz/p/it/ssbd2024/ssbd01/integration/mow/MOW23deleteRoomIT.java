package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOW23deleteRoomIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void deleteRoomPositiveTest() {
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

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/deleted/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("443")
                );
    }

    @Test
    public void deleteNotExistingRoomTest() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118999")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deleteRoomOptExceptionTest() {
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
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }

    @Test
    public void deleteRoomNoTokenTest() {
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

        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void deleteRoomBadRoleTest() {
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

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/rooms/room/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

}
