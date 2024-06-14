package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateLocationDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT.*;
import static pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT.baseUrl;

public class MOW16editLocationIT extends AbstractControllerIT {
    UpdateLocationDTO updateLocationDTO = new UpdateLocationDTO("nowa","testowa","69b","99-140","Lodz","Polska");
    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void editLocationPositiveTest() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl +"/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateLocationDTO))
                .when()
                .put(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value());
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"name\":\"nowa\"")
                );
    }
    @Test
    public void editLocationInvalidLocationName() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        UpdateLocationDTO updateLocationDTO = new UpdateLocationDTO("n","testowa","69b","99-140","Lodz","Polska");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateLocationDTO))
                .when()
                .put(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void editLocationInValidPostalCode() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        UpdateLocationDTO updateLocationDTO = new UpdateLocationDTO("nowa","testowa","69b","99-1400","Lodz","Polska");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateLocationDTO))
                .when()
                .put(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void editLocationWithoutToken() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateLocationDTO))
                .when()
                .put(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
    @Test
    public void editLocationBadRole() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateLocationDTO))
                .when()
                .put(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
    @Test
    public void editLocationInvalidEtag() throws JsonProcessingException {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl +"/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value());
        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateLocationDTO))
                .when()
                .put(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value());
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .body(objectMapper.writeValueAsString(updateLocationDTO))
                .when()
                .put(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());

    }
}
