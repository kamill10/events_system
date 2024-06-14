package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdateEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;

public class MOK11changeAnotherAccountEmailIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToAdminTest();
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void initChangeEmail() {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("jeszczeniema202401@proton.me");
        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .body(updateEmailDTO)
                .when()
                .post(baseUrl + "/accounts/change-email/" + "8b25c94f-f10f-4285-8eb2-39ee1c4002f1")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
