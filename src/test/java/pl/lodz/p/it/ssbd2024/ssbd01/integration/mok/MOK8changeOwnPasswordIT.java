package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdateMyPasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;

public class MOK8changeOwnPasswordIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToAdminTest();
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void initChangeOwnPasswordPositive() {
        UpdateMyPasswordDTO updatePasswordDTO = new UpdateMyPasswordDTO("P@ssw0rd", "P@ssw0rd1@");

        given()
                .header("Authorization", "Bearer " + participantToken)
                .contentType("application/json")
                .when()
                .body(updatePasswordDTO)
                .post(baseUrl + "/me/change-password")
                .then()
                .statusCode(200);
    }

    @Test
    public void initChangeOwnPasswordInvalidOldPassword() {
        UpdateMyPasswordDTO updatePasswordDTO = new UpdateMyPasswordDTO("P@ssw0rd231", "P@ssw0rd1@");

        given()
                .header("Authorization", "Bearer " + participantToken)
                .contentType("application/json")
                .when()
                .body(updatePasswordDTO)
                .post(baseUrl + "/me/change-password")
                .then()
                .statusCode(400);
    }

    @Test
    public void initChangeOwnPasswordSamePassword() {
        UpdateMyPasswordDTO updatePasswordDTO = new UpdateMyPasswordDTO("P@ssw0rd", "P@ssw0rd");

        given()
                .header("Authorization", "Bearer " + participantToken)
                .contentType("application/json")
                .when()
                .body(updatePasswordDTO)
                .post(baseUrl + "/me/change-password")
                .then()
                .statusCode(409);
    }

    @Test
    public void initChangeOwnPasswordInvalidNewPassword() {
        UpdateMyPasswordDTO updatePasswordDTO = new UpdateMyPasswordDTO("P@ssw0rd", "password");

        given()
                .header("Authorization", "Bearer " + participantToken)
                .contentType("application/json")
                .when()
                .body(updatePasswordDTO)
                .post(baseUrl + "/me/change-password")
                .then()
                .statusCode(400);
    }

}
