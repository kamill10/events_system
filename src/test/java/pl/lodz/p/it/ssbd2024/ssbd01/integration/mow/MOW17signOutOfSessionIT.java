package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOW17signOutOfSessionIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void signOutOfSessionPositiveTest() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .get(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .get(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("\"isNotCancelled\":false"),
                        containsString("\"availableSeats\":3")
                );
    }

    @Test
    public void signOutOfSessionWithoutToken() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .get(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void signOutOfSessionBadRole() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .get(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .delete(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void signOutOfSessionBadEtag() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .get(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }

    @Test
    public void signOutSessionCancelSomeonesTicket() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .get(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125511")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

        String etag = "c448c0528ba2b69118173bfceb9a4af9";

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125511")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void signOutOfSessionTicketAlreadyCancelled() {
        var response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .get(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.OK.value());

        String etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.OK.value());

        response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .when()
                .get(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.OK.value());

        etag = response.extract().header("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + participantToken)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/events/me/session/0b7edef0-55cb-4cc9-a7f2-95a662125512")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
