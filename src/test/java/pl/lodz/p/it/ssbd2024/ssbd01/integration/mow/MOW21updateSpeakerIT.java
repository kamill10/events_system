package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

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
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateSpeakerDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;

public class MOW21updateSpeakerIT extends AbstractControllerIT {

    @BeforeEach
    @SneakyThrows
    public void authenticate() {
        super.authenticationToParticipantTest();
        super.authenticationToManagerTest();
        super.authenticationToAdminTest();
    }

    @Test
    @SneakyThrows
    public void updateSpeakerAsManagerTest() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.OK.value());
        String json = response.extract().body().asString();
        var getSpeakerDTOList = super.objectMapper.readValue(json,new TypeReference<Page<GetSpeakerDTO>>(){});
        GetSpeakerDTO getSpeakerDTO = getSpeakerDTOList.stream().toList().getFirst();
        var responseGetSpeaker = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.OK.value());
        String speakerEtag = responseGetSpeaker.extract().header(HttpHeaders.ETAG);
        speakerEtag = speakerEtag.substring(1, speakerEtag.length() - 1);
        UpdateSpeakerDTO updateSpeakerDTO = new UpdateSpeakerDTO("JakiesImie", "JakiesNazwisko");
        Assertions.assertNotEquals(getSpeakerDTO.firstName(), updateSpeakerDTO.firstName());
        Assertions.assertNotEquals(getSpeakerDTO.lastName(), updateSpeakerDTO.lastName());
        var updateSpeakerResponse = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .header(HttpHeaders.IF_MATCH, speakerEtag)
                .body(super.objectMapper.writeValueAsString(updateSpeakerDTO))
                .when()
                .put(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.OK.value());
        String jsonUpdate = updateSpeakerResponse.extract().body().asString();
        var getSpeakerDTOUpdated = super.objectMapper.readValue(jsonUpdate, GetSpeakerDTO.class);
        Assertions.assertEquals(getSpeakerDTO.id(), getSpeakerDTOUpdated.id());
        Assertions.assertEquals(updateSpeakerDTO.firstName(), getSpeakerDTOUpdated.firstName());
        Assertions.assertEquals(updateSpeakerDTO.lastName(), getSpeakerDTOUpdated.lastName());
        var responseGetSpeakerUpdated = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.OK.value());
        String speakerEtagUpdated = responseGetSpeakerUpdated.extract().header(HttpHeaders.ETAG);
        Assertions.assertNotEquals(speakerEtag, speakerEtagUpdated);
        GetSpeakerDTO getSpeakerUpdated = super.objectMapper.readValue(responseGetSpeakerUpdated.extract().body().asString(), GetSpeakerDTO.class);
        Assertions.assertEquals(getSpeakerDTOUpdated.id(), getSpeakerUpdated.id());
        Assertions.assertEquals(getSpeakerDTOUpdated.firstName(), getSpeakerUpdated.firstName());
        Assertions.assertEquals(getSpeakerDTOUpdated.lastName(), getSpeakerUpdated.lastName());
    }
    @Test
    @SneakyThrows
    public void updateSpeakerNoIfMatchHeader() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.OK.value());
        String json = response.extract().body().asString();
        var getSpeakerDTOList = super.objectMapper.readValue(json,new TypeReference<Page<GetSpeakerDTO>>(){});
        GetSpeakerDTO getSpeakerDTO = getSpeakerDTOList.stream().toList().getFirst();
        UpdateSpeakerDTO updateSpeakerDTO = new UpdateSpeakerDTO("Jacek", "Graniecki");
        Assertions.assertNotEquals(getSpeakerDTO.firstName(), updateSpeakerDTO.firstName());
        Assertions.assertNotEquals(getSpeakerDTO.lastName(), updateSpeakerDTO.lastName());
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .body(super.objectMapper.writeValueAsString(updateSpeakerDTO))
                .when()
                .put(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    @SneakyThrows
    public void updateSpeakerInvalidEtagAsManager() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.OK.value());
        String json = response.extract().body().asString();
        var getSpeakerDTOList = super.objectMapper.readValue(json,new TypeReference<Page<GetSpeakerDTO>>(){});
        GetSpeakerDTO getSpeakerDTO = getSpeakerDTOList.stream().toList().getFirst();
        var responseGetSpeaker = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.OK.value());
        String speakerEtag = responseGetSpeaker.extract().header(HttpHeaders.ETAG);
        speakerEtag = speakerEtag.substring(1, speakerEtag.length() - 1);
        UpdateSpeakerDTO updateSpeakerDTO = new UpdateSpeakerDTO("Janek", "Granieckowski");
        Assertions.assertNotEquals(getSpeakerDTO.firstName(), updateSpeakerDTO.firstName());
        Assertions.assertNotEquals(getSpeakerDTO.lastName(), updateSpeakerDTO.lastName());
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .header(HttpHeaders.IF_MATCH, speakerEtag)
                .body(super.objectMapper.writeValueAsString(updateSpeakerDTO))
                .when()
                .put(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.OK.value());
        updateSpeakerDTO = new UpdateSpeakerDTO("Janekkk", "Granieckowskiii");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .header(HttpHeaders.IF_MATCH, speakerEtag)
                .body(super.objectMapper.writeValueAsString(updateSpeakerDTO))
                .when()
                .put(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());

    }

    @Test
    @SneakyThrows
    public void updateSpeakerEmptyFirstNameTestAsManager() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.OK.value());
        String json = response.extract().body().asString();
        var getSpeakerDTOList = super.objectMapper.readValue(json,new TypeReference<Page<GetSpeakerDTO>>(){});
        GetSpeakerDTO getSpeakerDTO = getSpeakerDTOList.stream().toList().getFirst();
        var responseGetSpeaker = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.OK.value());
        String speakerEtag = responseGetSpeaker.extract().header(HttpHeaders.ETAG);
        speakerEtag = speakerEtag.substring(1, speakerEtag.length() - 1);
        UpdateSpeakerDTO updateSpeakerDTO = new UpdateSpeakerDTO("", "Graniecki");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .header(HttpHeaders.IF_MATCH, speakerEtag)
                .body(super.objectMapper.writeValueAsString(updateSpeakerDTO))
                .when()
                .put(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SneakyThrows
    public void updateSpeakerEmptyLastNameAsManager() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.OK.value());
        String json = response.extract().body().asString();
        var getSpeakerDTOList = super.objectMapper.readValue(json,new TypeReference<Page<GetSpeakerDTO>>(){});
        GetSpeakerDTO getSpeakerDTO = getSpeakerDTOList.stream().toList().getFirst();
        var responseGetSpeaker = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.OK.value());
        String speakerEtag = responseGetSpeaker.extract().header(HttpHeaders.ETAG);
        speakerEtag = speakerEtag.substring(1, speakerEtag.length() - 1);
        UpdateSpeakerDTO updateSpeakerDTO = new UpdateSpeakerDTO("Jacek", "");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .header(HttpHeaders.IF_MATCH, speakerEtag)
                .body(super.objectMapper.writeValueAsString(updateSpeakerDTO))
                .when()
                .put(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SneakyThrows
    public void updateSpeakerTooShortFirstNameAsManager() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.OK.value());
        String json = response.extract().body().asString();
        var getSpeakerDTOList = super.objectMapper.readValue(json,new TypeReference<Page<GetSpeakerDTO>>(){});
        GetSpeakerDTO getSpeakerDTO = getSpeakerDTOList.stream().toList().getFirst();
        var responseGetSpeaker = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.OK.value());
        String speakerEtag = responseGetSpeaker.extract().header(HttpHeaders.ETAG);
        speakerEtag = speakerEtag.substring(1, speakerEtag.length() - 1);
        UpdateSpeakerDTO updateSpeakerDTO = new UpdateSpeakerDTO("J", "Graniecki");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .header(HttpHeaders.IF_MATCH, speakerEtag)
                .body(super.objectMapper.writeValueAsString(updateSpeakerDTO))
                .when()
                .put(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SneakyThrows
    public void updateSpeakerTooShortLastNameAsManager() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.OK.value());
        String json = response.extract().body().asString();
        var getSpeakerDTOList = super.objectMapper.readValue(json,new TypeReference<Page<GetSpeakerDTO>>(){});
        GetSpeakerDTO getSpeakerDTO = getSpeakerDTOList.stream().toList().getFirst();
        var responseGetSpeaker = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.OK.value());
        String speakerEtag = responseGetSpeaker.extract().header(HttpHeaders.ETAG);
        speakerEtag = speakerEtag.substring(1, speakerEtag.length() - 1);
        UpdateSpeakerDTO updateSpeakerDTO = new UpdateSpeakerDTO("Jacek", "G");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .header(HttpHeaders.IF_MATCH, speakerEtag)
                .body(super.objectMapper.writeValueAsString(updateSpeakerDTO))
                .when()
                .put(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SneakyThrows
    public void updateSpeakerTooLongFirstNameAsManager() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.OK.value());
        String json = response.extract().body().asString();
        var getSpeakerDTOList = super.objectMapper.readValue(json, new TypeReference<Page<GetSpeakerDTO>>() {
        });
        GetSpeakerDTO getSpeakerDTO = getSpeakerDTOList.stream().toList().getFirst();
        var responseGetSpeaker = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.OK.value());
        String speakerEtag = responseGetSpeaker.extract().header(HttpHeaders.ETAG);
        speakerEtag = speakerEtag.substring(1, speakerEtag.length() - 1);
        UpdateSpeakerDTO updateSpeakerDTO = new UpdateSpeakerDTO(
                "Jacekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk,",
                "Graniecki");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .header(HttpHeaders.IF_MATCH, speakerEtag)
                .body(super.objectMapper.writeValueAsString(updateSpeakerDTO))
                .when()
                .put(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SneakyThrows
    public void updateSpeakerTooLongLastNameAsManager() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers")
                .then()
                .statusCode(HttpStatus.OK.value());
        String json = response.extract().body().asString();
        var getSpeakerDTOList = super.objectMapper.readValue(json, new TypeReference<Page<GetSpeakerDTO>>() {
        });
        GetSpeakerDTO getSpeakerDTO = getSpeakerDTOList.stream().toList().getFirst();
        var responseGetSpeaker = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .when()
                .get(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.OK.value());
        String speakerEtag = responseGetSpeaker.extract().header(HttpHeaders.ETAG);
        speakerEtag = speakerEtag.substring(1, speakerEtag.length() - 1);
        UpdateSpeakerDTO updateSpeakerDTO = new UpdateSpeakerDTO(
                "Jacek",
                "Granieckiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .header(HttpHeaders.IF_MATCH, speakerEtag)
                .body(super.objectMapper.writeValueAsString(updateSpeakerDTO))
                .when()
                .put(super.baseUrl + "/speakers/" + getSpeakerDTO.id())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SneakyThrows
    public void updateNonExistentSpeakerAsManagerTest() {
        UpdateSpeakerDTO updateSpeakerDTO = new UpdateSpeakerDTO("Jacek", "Graniecki");
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.managerToken)
                .header(HttpHeaders.IF_MATCH, "test")
                .body(super.objectMapper.writeValueAsString(updateSpeakerDTO))
                .when()
                .put(super.baseUrl + "/speakers/00000000-0000-0000-0000-000000000022")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void updateSpeakerAsParticipantTest() {
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.participantToken)
                .when()
                .put(super.baseUrl + "/speakers/22")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void updateSpeakerAsAdminTest() {
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + super.adminToken)
                .when()
                .put(super.baseUrl + "/speakers/22")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void updateSpeakerWithoutTokenTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .put(super.baseUrl + "/speakers/22")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }




}
