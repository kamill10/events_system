package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateSpeakerDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSpeakerDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;

public class MOW20addSpeakerIT extends AbstractControllerIT {

    @BeforeEach
    @SneakyThrows
    public void authenticate() {
        super.authenticationToParticipantTest();
        super.authenticationToManagerTest();
        super.authenticationToAdminTest();
    }

    @Test
    @SneakyThrows
    public void addSpeakerAsManagerTest() {
        CreateSpeakerDTO createSpeakerDTO = new CreateSpeakerDTO("Jacek","Graniecki");
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .body(super.objectMapper.writeValueAsString(createSpeakerDTO))
                .when()
                .post(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.OK.value());
        String json = response.extract().body().asString();
        var getSpeakerDTO = super.objectMapper.readValue(json, GetSpeakerDTO.class);
        Assertions.assertNotNull(getSpeakerDTO.id());
        Assertions.assertEquals(createSpeakerDTO.firstName(), getSpeakerDTO.firstName());
        Assertions.assertEquals(createSpeakerDTO.lastName(), getSpeakerDTO.lastName());
    }

    @Test
    public void addSpeakerEmptyFirstNameAsManagerTest() {
        CreateSpeakerDTO createSpeakerDTO = new CreateSpeakerDTO("","Graniecki");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .body(createSpeakerDTO)
                .when()
                .post(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
    @Test
    public void addSpeakerTooShortFirstNameAsManagerTest() {
        CreateSpeakerDTO createSpeakerDTO = new CreateSpeakerDTO("J","Graniecki");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .body(createSpeakerDTO)
                .when()
                .post(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
    @Test
    public void addSpeakerTooLongFirstNameAsManagerTest() {
        CreateSpeakerDTO createSpeakerDTO = new CreateSpeakerDTO(
                "Jacekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk,",
                "Graniecki");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .body(createSpeakerDTO)
                .when()
                .post(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSpeakerEmptyLastNameAsManagerTest() {
        CreateSpeakerDTO createSpeakerDTO = new CreateSpeakerDTO("Jacek","");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .body(createSpeakerDTO)
                .when()
                .post(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSpeakerTooShortLastNameAsManagerTest() {
        CreateSpeakerDTO createSpeakerDTO = new CreateSpeakerDTO("Jacek","G");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .body(createSpeakerDTO)
                .when()
                .post(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSpeakerTooLongLastNameAsManagerTest() {
        CreateSpeakerDTO createSpeakerDTO = new CreateSpeakerDTO("Jacek",
                "Granieckiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .body(createSpeakerDTO)
                .when()
                .post(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addSpeakerAsParticipantTest() {
        CreateSpeakerDTO createSpeakerDTO = new CreateSpeakerDTO("Jacek","Graniecki");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.participantToken)
                .body(createSpeakerDTO)
                .when()
                .post(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void addSpeakerAsAdminTest() {
        CreateSpeakerDTO createSpeakerDTO = new CreateSpeakerDTO("Jacek","Graniecki");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.adminToken)
                .body(createSpeakerDTO)
                .when()
                .post(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void addSpeakerWithoutTokenTest() {
        CreateSpeakerDTO createSpeakerDTO = new CreateSpeakerDTO("Jacek","Graniecki");
        given()
                .contentType(ContentType.JSON)
                .body(createSpeakerDTO)
                .when()
                .post(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }


}
