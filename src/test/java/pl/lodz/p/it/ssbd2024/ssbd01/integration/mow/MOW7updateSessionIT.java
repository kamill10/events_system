package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateSessionDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOW7updateSessionIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void updateSessionPositiveTest() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"room\":{\"id\":\"78f0f497-10b7-4478-9a28-c9dc86118e67\""),
                        containsString("\"speaker\":{\"id\":\"713c84a3-03bd-4206-ac5c-ecf8d7d04ae6\""),
                        containsString("\"name\":\"Dlaczego Spring bez buta jest najlepszy?\""),
                        containsString("\"description\":\"Wykład o najlepszym frameworku na świecie\""),
                        containsString("\"startTime\":\"" + updateSessionDTO.startDate().toString() + "\""),
                        containsString("\"endTime\":\"" + updateSessionDTO.endDate().toString() + "\""),
                        containsString("\"maxSeats\":25")
                );
    }

    @Test
    public void updateSessionWithoutToken() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void updateSessionWithBadEtag() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag + "a")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }

    @Test
    public void updateSessionRaceCondition() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        var response2 = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag2 = response2.extract().header("ETag");
        etag2 = etag2.substring(1, etag2.length() - 1);

        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag2)
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }

    @Test
    public void updateSessionWhenStartDateIsAfterEndDate() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenStartDateIsInPast() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2020, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenSessionExistsOutsideEventRange() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 14, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenRoomNotExists() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e11"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void updateSessionWhenSpeakerNotExists() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04a11"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void updateSessionWhenSessionNotExists() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void updateSessionWhenMaxRoomsAreGreaterThanRoomCapacity() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                100
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d2")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void updateSessionWhenRoomIdIsNull() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                null,
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenSpeakerIdIsNull() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                null,
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenNameIsNull() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                null,
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenNameIsEmpty() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenNameIsBlank() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "      ",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenNameIsTooShort() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "a",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenNameIsTooLong() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "A".repeat(129),
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenDescriptionIsNull() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                null,
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenDescriptionIsEmpty() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenDescriptionIsBlank() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "      ",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenDescriptionIsTooShort() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "a",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenDescriptionIsTooLong() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "A".repeat(1025),
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenStartTimeIsNull() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                null,
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenEndDateIsNull() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                null,
                25
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateSessionWhenMaxSeatsIsNull() {
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Spring bez buta jest najlepszy?",
                "Wykład o najlepszym frameworku na świecie",
                LocalDateTime.of(2024, 11, 11, 1, 1, 1),
                LocalDateTime.of(2024, 11, 11, 2, 1, 1),
                null
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", "1")
                .when()
                .body(updateSessionDTO)
                .put(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


}
