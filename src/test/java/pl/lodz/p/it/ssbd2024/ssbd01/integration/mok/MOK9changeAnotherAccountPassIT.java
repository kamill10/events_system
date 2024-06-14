package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdateEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.UpdatePasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MOK9changeAnotherAccountPassIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToAdminTest();
        authenticationToParticipantTest();
        authenticationToManagerTest();
    }

    @Test
    public void initPasswordChangePositive() {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("admin202401@proton.me");
        given()
                .header("Authorization", "Bearer " + adminToken)
                .body(updateEmailDTO)
                .contentType("application/json")
                .when()
                .post(baseUrl + "/accounts/change-password")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void blockAccountWhenPasswordChangeByAdmin() throws JsonProcessingException {
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO("admin202401@proton.me");
        String token = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(updateEmailDTO)
                .when()
                .post(baseUrl + "/accounts/change-password")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();
        assertNotNull(token);

        //account is blocked and user cannot log in
        LoginDTO loginDTO = new LoginDTO("testAdmin", "P@ssw0rd");
        ValidatableResponse response = given()
                .contentType("application/json")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .body(objectMapper.writeValueAsString(loginDTO))
                .when()
                .post(baseUrl + "/auth/authenticate")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void sendTokenWhenPasswordChangeByAdminButTokenNotExist() {
        UpdatePasswordDTO password = new UpdatePasswordDTO("dsafdvcxsd");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(password)
                .when()
                .post(baseUrl + "/accounts/change-password/token/" + "4234")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().asString();
    }
}
