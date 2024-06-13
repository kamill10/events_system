package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOW12cancelEventIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void cancelEventPositiveTest() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"isNotCanceled\":false")
                );
    }

    @Test
    public void cancelEventWithoutToken() {
        var response = given()
                .contentType("application/json")
                .header("Accept-Language", "en-US")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void cancelEventBadRole() {
        var response = given()
                .contentType("application/json")
                .header("Accept-Language", "en-US")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void cancelEventAlreadyCancelled() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.OK.value());

         response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.OK.value());

        etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void cancelEventBadEtag() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/dbe1b405-aed0-4d0a-bda7-50938e7b45f1")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }



}
