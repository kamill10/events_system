package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;

public class MOK17verifyAccountIT extends AbstractControllerIT {

    @Test
    public void testVerifyAccountWithInvalidToken() {
        given()
                .contentType("application/json")
                .when()
                .post(baseUrl + "/auth/verify_account/invalidToken")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
