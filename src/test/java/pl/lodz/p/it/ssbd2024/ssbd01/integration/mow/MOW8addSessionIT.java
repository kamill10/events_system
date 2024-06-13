package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateSessionDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOW8addSessionIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void addSessionPositiveTest() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.OK.value());


        String sessionId = response.extract().asString().substring(1, response.extract().asString().length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/" + sessionId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("Dlaczego Tede kolegą jest?"),
                        containsString("Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja"),
                        containsString("2024-09-21T01:01:01"),
                        containsString("2024-09-21T02:01:01"),
                        containsString("20")
                );
    }

    @Test
    public void addSessionWithoutToken() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void addSessionWithBadRole() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void addSessionWithStartDateInPast() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2020, 9, 21, 1, 1, 1),
                LocalDateTime.of(2020, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWithStartDateAfterEndDate() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenEventNotExists() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33292"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void addSessionWhenSpeakerNotExists() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e68"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void addSessionWhenRoomNotExists() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae7"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void addSessionWhenEventIdIsNull() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                null,
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenRoomIdIsNull() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                null,
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenSpeakerIdIsNull() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                null,
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenNameIsEmpty() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenNameIsBlank() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "        ",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenNameIsTooShort() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "A",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenNameIsTooLong() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "A".repeat(129),
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenDescriptionIsEmpty() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenDescriptionIsBlank() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "        ",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenDescriptionIsTooShort() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "A",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenDescriptionIsTooLong() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "A".repeat(1025),
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenStartDateIsNull() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                null,
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenEndDateIsNull() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                null,
                20
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSessionWhenMaxSeatsIsNull() {
        CreateSessionDTO createSessionDTO = new CreateSessionDTO(
                UUID.fromString("fffc5e88-3054-4776-9189-4e64f1a33291"),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("713c84a3-03bd-4206-ac5c-ecf8d7d04ae6"),
                "Dlaczego Tede kolegą jest?",
                "Wykład o tolerancji prowadzony przez Ryszarda Andrzejewskiego ps. Peja",
                LocalDateTime.of(2024, 9, 21, 1, 1, 1),
                LocalDateTime.of(2024, 9, 21, 2, 1, 1),
                null
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createSessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


}
