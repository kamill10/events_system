package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOW14deleteLocationIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void deleteLocationPositiveTest() {
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
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"isActive\":false")
                );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/deleted/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"isActive\":false")
                );

    }

    @Test
    public void deleteLocationWithoutToken() {
        var response = given()
                .contentType("application/json")
                .header("Accept-Language", "en-US")
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void deleteLocationWithBadRole() {
        var response = given()
                .contentType("application/json")
                .header("Accept-Language", "en-US")
                .header("Authorization", "Bearer " + managerToken)
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
                .when()
                .delete(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void deleteLocationThatIsAlreadyDeleted() {
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
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"isActive\":false")
                );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/deleted/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"isActive\":false")
                );

        var response2 = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag2 = response2.extract().header("ETag");
        etag2 = etag2.substring(1, etag2.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag2)
                .when()
                .delete(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void deleteLocationWithBadEtag() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 5);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }

    @Test
    public void deleteLocationWhenRaceCondition() {
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

        var response2 = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag2 = response2.extract().header("ETag");
        etag2 = etag2.substring(1, etag2.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"isActive\":false")
                );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "en-US")
                .when()
                .get(baseUrl + "/rooms/room/deleted/78f0f497-10b7-4478-9a28-c9dc86118e67")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"isActive\":false")
                );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("If-Match", etag2)
                .when()
                .delete(baseUrl + "/location/da7cfcfa-5f1c-4a85-8f93-1022f28f747a")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());

    }


}
