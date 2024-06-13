package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSpeakerDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;


import static io.restassured.RestAssured.given;


public class MOW19getAllSpeakersIT extends AbstractControllerIT {

    @BeforeEach
    @SneakyThrows
    public void authenticate() {
        super.authenticationToParticipantTest();
        super.authenticationToManagerTest();
        super.authenticationToAdminTest();
    }

    @Test
    @SneakyThrows
    public void getAllSpeakersAsManagerTest() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.OK.value());
        String json = response.extract().body().asString();
        var getSpeakerDTOList = super.objectMapper.readValue(json,new TypeReference<Page<GetSpeakerDTO>>(){});
        Assertions.assertFalse(getSpeakerDTOList.isEmpty());
        Assertions.assertTrue(getSpeakerDTOList.getTotalElements() >= 4);
    }

    @Test
    public void getAllSpeakersAsParticipantTest() {
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.participantToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void getAllSpeakersAsAdminTest() {
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.adminToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void getAllSpeakersWithoutTokenTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @SneakyThrows
    public void getSpeakerByIdAsManagerTest() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.OK.value());
        String json = response.extract().body().asString();
        var getSpeakerDTOList = super.objectMapper.readValue(json,new TypeReference<Page<GetSpeakerDTO>>(){});
        getSpeakerDTOList.forEach(speaker ->{
            var speakerResponse = given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                    .when()
                    .get(super.baseUrl + "/speakers/" + speaker.id())
                    .then()
                    .statusCode(HttpStatus.OK.value());
            Assertions.assertFalse(speakerResponse.extract().header(HttpHeaders.ETAG).isBlank());
            String speakerJson = speakerResponse.extract().body().asString();
            try {
                var getSpeakerDTO = super.objectMapper.readValue(speakerJson,GetSpeakerDTO.class);
                Assertions.assertEquals(speaker,getSpeakerDTO);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void getSpeakerByIdNonExistentIdAsManagerTest() {
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers/00000000-0000-0000-0000-000000000000")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getSpeakerByIdAsParticipantTest() {
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.participantToken)
                .when()
                .get(super.baseUrl + "/speakers/f3c50886-bb5a-451c-99a3-6a79a6329cb6")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void getSpeakerByIdAsAdminTest() {
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.adminToken)
                .when()
                .get(super.baseUrl + "/speakers/f3c50886-bb5a-451c-99a3-6a79a6329cb6")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void getSpeakerByIdWithoutTokenTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(super.baseUrl + "/speakers/f3c50886-bb5a-451c-99a3-6a79a6329cb6")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
