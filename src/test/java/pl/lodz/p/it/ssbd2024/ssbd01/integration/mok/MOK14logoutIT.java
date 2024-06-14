package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;

public class MOK14logoutIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToParticipantTest();
    }

    @Test
    public void logoutTest() throws JsonProcessingException {
        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + participantToken)
                .when()
                .post(baseUrl + "/auth/logout")
                .then()
                .statusCode(HttpStatus.OK.value());

    }
}
