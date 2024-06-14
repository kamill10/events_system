package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MOK2authenticateIT extends AbstractControllerIT {

    @Test
    public void successfulLoginTest() {
        LoginDTO loginDTO = new LoginDTO("marcinKaczkan21", "P@ssw0rd");

        given()
                .contentType("application/json")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .body(loginDTO)
                .when()
                .post(baseUrl + "/auth/authenticate")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void failedLoginAttemptsTest() {
        LoginDTO loginDTO = new LoginDTO("marcinKaczkan21", "invalidPassword");

        given()
                .contentType("application/json")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .body(loginDTO)
                .when()
                .post(baseUrl + "/auth/authenticate")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        given()
                .contentType("application/json")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .body(loginDTO)
                .when()
                .post(baseUrl + "/auth/authenticate")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        given()
                .contentType("application/json")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .body(loginDTO)
                .when()
                .post(baseUrl + "/auth/authenticate")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        LoginDTO newLoginDto = new LoginDTO("marcinKaczkan21", "P@ssw0rd");

        given()
                .contentType("application/json")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .body(newLoginDto)
                .when()
                .post(baseUrl + "/auth/authenticate")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body(
                        containsString("User account is locked")
                );
    }

}
