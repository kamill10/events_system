package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateSessionDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOW11updateEventIT extends AbstractControllerIT {

    private String eventId;
    private String etag;
    private final Random random = new Random();

    private void setupEventAndEtag() {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );

        String createEventResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "pl-PL")
                .body(eventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .body()
                .asString();
        eventId = createEventResponse.substring(1, createEventResponse.length() - 1);

        String getEventResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "pl-PL")
                .when()
                .get(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .header(HttpHeaders.ETAG);
        etag = getEventResponse.substring(1, getEventResponse.length() - 1);
    }

    @BeforeEach
    public void auth() throws JsonProcessingException {
        authenticationToManagerTest();
        authenticationToParticipantTest();
        setupEventAndEtag();
    }

    @Test
    public void updateEventNoJWTPresentTest() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(random.nextInt(10))
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void updateEventNoAccessTest() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(random.nextInt(10))
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + participantToken)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void updateEventPositiveTest() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(random.nextInt(10))
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .header(HttpHeaders.IF_MATCH, etag)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.OK.value());
    }
    @Test
    public void updateEventNameTooShortTest() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(2, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(random.nextInt(10))
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .header(HttpHeaders.IF_MATCH, etag)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateEventNameTooLongTest() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(129, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(random.nextInt(10))
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .header(HttpHeaders.IF_MATCH, etag)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateEventDescriptionTooShortTest() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(2, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(random.nextInt(10))
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .header(HttpHeaders.IF_MATCH, etag)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateEventDescriptionTooLongTest() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(2137, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(random.nextInt(10))
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .header(HttpHeaders.IF_MATCH, etag)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateEventStartDateNullTest() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                null,
                LocalDateTime.now().plusDays(random.nextInt(10))
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .header(HttpHeaders.IF_MATCH, etag)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateEventEndDateNullTest() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                null
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .header(HttpHeaders.IF_MATCH, etag)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateEventThrowsEventStartDateInPastException() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.of(2020, Month.AUGUST, 10, 10, 10, 10),
                LocalDateTime.of(2020, Month.AUGUST, 14, 10, 10, 10)
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .header(HttpHeaders.IF_MATCH, etag)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateEventThrowsEventStartDateAfterEndDateException() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1)
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .header(HttpHeaders.IF_MATCH, etag)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateEventThrowsSessionsExistOutsideRangeException() {
        CreateSessionDTO sessionDTO = new CreateSessionDTO(
                UUID.fromString(eventId),
                UUID.fromString("78f0f497-10b7-4478-9a28-c9dc86118e67"),
                UUID.fromString("f3c50886-bb5a-451c-99a3-6a79a6329cb5"),
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(2),
                random.nextInt(10, 30)
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .body(sessionDTO)
                .when()
                .post(baseUrl + "/sessions")
                .then()
                .statusCode(HttpStatus.OK.value());

        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .header(HttpHeaders.IF_MATCH, etag)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + eventId)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateEventThrowsEventNotFoundException() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .header(HttpHeaders.IF_MATCH, etag)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/" + UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
