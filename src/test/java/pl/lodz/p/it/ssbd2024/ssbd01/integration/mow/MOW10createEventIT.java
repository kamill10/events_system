package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import groovy.json.JsonOutput;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOW10createEventIT extends AbstractControllerIT {
    @BeforeEach
    public void auth() throws JsonProcessingException {
        authenticationToManagerTest();
        authenticationToParticipantTest();
    }

    @Test
    public void createEventNoJWTPresentTest() {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .body(eventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void createEventNoAccessTest() {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + participantToken)
                .body(eventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void createEventPositiveTest() throws JsonProcessingException {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );

        var response = given()
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

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "pl-PL")
                .when()
                .get(baseUrl + "/events/" + response.substring(1, response.length() - 1))
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void createEventThrowsEventStartDateInPastExceptionTest() {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.of(2020, Month.AUGUST, 10, 10, 10, 10),
                LocalDateTime.of(2020, Month.AUGUST, 14, 10, 10, 10)
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "pl-PL")
                .body(eventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void createEventThrowsEventStartDateAfterEndDateExceptionTest() {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now()
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "pl-PL")
                .body(eventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void createEventNameTooShortTest() {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(2, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .body(eventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
    @Test
    public void createEventNameTooLongTest() {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(129, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .body(eventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
    @Test
    public void createEventDescriptionTooShortTest() {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(3, true, false),
                "",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .body(eventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void createEventDescriptionTooLongTest() {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(1025, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .body(eventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void createEventStartDateNullTest() {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(1025, true, false),
                null,
                LocalDateTime.now().plusDays(1)
        );
        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .body(eventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void createEventEndDateNullTest() {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                null
        );
        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .body(eventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void createEventDuplicatedNameExceptionTest() {
        CreateEventDTO eventDTO = new CreateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "pl-PL")
                .body(eventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        CreateEventDTO newEventDTO = new CreateEventDTO(
                eventDTO.name(),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "pl-PL")
                .body(newEventDTO)
                .when()
                .post(baseUrl + "/events")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }
}
