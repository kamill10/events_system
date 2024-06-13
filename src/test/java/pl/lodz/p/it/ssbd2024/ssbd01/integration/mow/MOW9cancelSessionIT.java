package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOW9cancelSessionIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void cancelSessionPositiveTest() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"isActive\":false")
                );
    }

    @Test
    public void cancelSessionWithoutToken() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void cancelSessionWithBadRole() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .delete(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void cancelSessionThatIsAlreadyCanceled() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .delete(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"isActive\":false")
                );

        var response2 = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag2 = response2.extract().header("ETag");
        etag2 = etag2.substring(1, etag2.length() - 1);

        given()
                .contentType("application/json")
                .header("If-Match", etag2)
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .delete(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void cancelSessionWithBadEtag() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("If-Match", etag + "1")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .delete(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }

    @Test
    public void cancelSessionWithBadId() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d1")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void cancelSessionRaceCondition() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        var response2 = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag2 = response2.extract().header("ETag");
        etag2 = etag2.substring(1, etag2.length() - 1);

        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .delete(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/sessions/manager/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"isActive\":false")
                );

        given()
                .contentType("application/json")
                .header("If-Match", etag2)
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .delete(baseUrl + "/sessions/4b2555e9-61f1-4c1d-9d7a-f425696eb2d3")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }

}
