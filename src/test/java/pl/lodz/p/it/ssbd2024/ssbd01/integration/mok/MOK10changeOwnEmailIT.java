package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdateEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdateMyEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;

public class MOK10changeOwnEmailIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToAdminTest();
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void initEmailUpdatePositive() throws JsonProcessingException {
        UpdateMyEmailDTO updateEmailDTO = new UpdateMyEmailDTO("P@ssw0rd", "ssbd01@proton.me");

        given()
                .header("Authorization", "Bearer " + participantToken)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(updateEmailDTO))
                .when()
                .post(baseUrl + "/me/email")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void testUpdateMyEmailUnAuthorized() throws JsonProcessingException {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("ssbd01@proton.me");
        given()
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(updateEmailDTO))
                .when()
                .patch(baseUrl + "/me/email")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
