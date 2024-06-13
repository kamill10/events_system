package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOW11updateEventIT extends AbstractControllerIT {

    private String eventId;
    private String etag;
    private String eventName;
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
        eventName = eventDTO.name();
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
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body(
                        containsString("HTTP Status 403 – Forbidden")
                );
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
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body(
                        containsString("HTTP Status 403 – Forbidden")
                );
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
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString(eventDTO.name()),
                        containsString(eventDTO.description()),
                        containsString(eventDTO.startDate().withHour(0).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS).toString()),
                        containsString(eventDTO.endDate().withHour(23).withMinute(59).withSecond(59).truncatedTo(ChronoUnit.SECONDS).toString())
                );
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
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        containsString(ExceptionMessages.INCORRECT_NAME)
                );
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
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        containsString(ExceptionMessages.INCORRECT_NAME)
                );
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
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        containsString(ExceptionMessages.INCORRECT_DESCRIPTION)
                );
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
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        containsString(ExceptionMessages.INCORRECT_DESCRIPTION)
                );
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
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        containsString(ExceptionMessages.FIELD_REQUIRED)
                );
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
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        containsString(ExceptionMessages.FIELD_REQUIRED)
                );
    }

    @Test
    public void updateEventThrowsEventStartDateInPastException() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
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
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        containsString(ExceptionMessages.EVENT_START_IN_PAST)
                );
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
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        containsString(ExceptionMessages.EVENT_START_AFTER_END)
                );
    }

    @Test
    public void updateEventThrowsSessionsExistOutsideRangeException() {
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
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
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        containsString(ExceptionMessages.EVENT_START_AFTER_END)
                );
    }
}
