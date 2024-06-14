package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdateEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdatePasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;

public class MOK3passwordResetIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToAdminTest();
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void initResetAccountPassword() {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("admin202401@proton.me");
        given()
                .contentType("application/json")
                .body(updateEmailDTO)
                .when()
                .post(baseUrl + "/accounts/reset-password")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void confirmPasswordResetInvalidToken() {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("newPassword123@");
        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .body(updatePasswordDTO)
                .when()
                .patch(baseUrl + "/accounts/reset-password/token/2137")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void initPasswordResetEmailNotFound() {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("niematakiego@proton.me");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(updateEmailDTO)
                .when()
                .post(baseUrl + "/accounts/reset-password")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
