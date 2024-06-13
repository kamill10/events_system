package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateLocationDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOW15addLocationIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void addLocationPositiveTest() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "testName",
                "testStreet",
                "testNumber",
                "12-123",
                "testCity",
                "testCountry"
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"name\":\"testName\""),
                        containsString("\"street\":\"testStreet\""),
                        containsString("\"buildingNumber\":\"testNumber\""),
                        containsString("\"postalCode\":\"12-123\""),
                        containsString("\"city\":\"testCity\""),
                        containsString("\"country\":\"testCountry\"")
                );
    }

    @Test
    public void addLocationWithoutToken() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "testName",
                "testStreet",
                "testNumber",
                "12-123",
                "testCity",
                "testCountry"
        );
        given()
                .contentType("application/json")
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void addLocationWithBadRole() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "testName",
                "testStreet",
                "testNumber",
                "12-123",
                "testCity",
                "testCountry"
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void addLocationWithSameName() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "testName",
                "testStreet",
                "testNumber",
                "12-123",
                "testCity",
                "testCountry"
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void addLocationWithEmptyName() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "",
                "testStreet",
                "testNumber",
                "12-123",
                "testCity",
                "testCountry"
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addLocationWithTooShortName() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "a",
                "testStreet",
                "testNumber",
                "12-123",
                "testCity",
                "testCountry"
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addLocationWithTooLongName() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "alvscermgyrpmexcauxpelvucriwncmendddmimrlfusmxvyyeqwjqnxebcsgrgfopoyebfnkqzbyzsuuifulrlvxtrxnxlxbvsdnyaxdkesbmagnlxlgoyhkfcgwehfg",
                "testStreet",
                "testNumber",
                "12-123",
                "testCity",
                "testCountry"
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addLocationWithEmptyStreet() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "testName",
                "",
                "testNumber",
                "12-123",
                "testCity",
                "testCountry"
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addLocationWithEmptyBuildingNumber() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "testName",
                "testStreet",
                "",
                "12-123",
                "testCity",
                "testCountry"
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addLocationWithEmptyPostalCode() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "testName",
                "testStreet",
                "testNumber",
                "",
                "testCity",
                "testCountry"
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addLocationWithBadPostalCode() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "testName",
                "testStreet",
                "testNumber",
                "123-123",
                "testCity",
                "testCountry"
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addLocationWithEmptyCity() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "testName",
                "testStreet",
                "testNumber",
                "12-123",
                "",
                "testCountry"
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addLocationWithEmptyCountry() {
        CreateLocationDTO createLocationDTO = new CreateLocationDTO(
                "testName",
                "testStreet",
                "testNumber",
                "12-123",
                "testCity",
                ""
        );
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .body(createLocationDTO)
                .when()
                .post(baseUrl + "/location")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

}
