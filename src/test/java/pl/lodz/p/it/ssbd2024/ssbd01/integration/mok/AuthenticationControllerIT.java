package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.create.CreateAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.LanguageEnum;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;


public class AuthenticationControllerIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToAdminTest();
    }

    @Test
    public void testRegisterAccount() {
        CreateAccountDTO createAccountDTO =
                new CreateAccountDTO("user", "P@ssw0rd", "isrpgrupa1@proton.me", 1, "firstName", "lastName", LanguageEnum.POLISH);
        given()
                .contentType("application/json")
                .body(createAccountDTO)
                .when()
                .post(baseUrl + "/auth/register")
                .then()
                .statusCode(HttpStatus.CREATED.value());
        given()
                .contentType("application/json")
                .body(createAccountDTO)
                .when()
                .post(baseUrl + "/auth/register")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void testRegisterAccountWithAnonymousUserName() {
        CreateAccountDTO createAccountDTO =
                new CreateAccountDTO("anonymous", "P@ssw0rd", "isrpgrupa2@proton.me", 1, "firstName", "lastName", LanguageEnum.POLISH);
        given()
                .contentType("application/json")
                .body(createAccountDTO)
                .when()
                .post(baseUrl + "/auth/register")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testRegisterAccountWithViolations() {
        CreateAccountDTO createAccountDTO = new CreateAccountDTO("messi", "password"
                , "isrpgrupa1@proton.me", 1, "", "lastName", LanguageEnum.POLISH);
        given()
                .contentType("application/json")
                .body(createAccountDTO)
                .when()
                .post(baseUrl + "/auth/register")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void testRegisterAccountWithNotUniqueUsername() {
        CreateAccountDTO createAccountDTO =
                new CreateAccountDTO("testAdmin", "P@ssw0rd", "isrpgrupa1@proton.me", 1, "firstName", "lastName", LanguageEnum.POLISH);
        given()
                .contentType("application/json")
                .body(createAccountDTO)
                .when()
                .post(baseUrl + "/auth/register")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void testRegisterAccountWithNotUniqueEmail() {
        CreateAccountDTO createAccountDTO = new CreateAccountDTO("testAdmin", "P@ssw0rd"
                , "admin202401@proton.me", 1, "firstName", "lastName", LanguageEnum.POLISH);
        given()
                .contentType("application/json")
                .body(createAccountDTO)
                .when()
                .post(baseUrl + "/auth/register")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void testVerifyAccountWithInvalidToken() {
        given()
                .contentType("application/json")
                .when()
                .post(baseUrl + "/auth/verify_account/invalidToken")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
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

        ValidatableResponse response = given()
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

    @Test
    public void refreshTokenTest() {
        var resp = given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .when()
                .post(baseUrl + "/auth/refresh-token")
                .then()
                .statusCode(HttpStatus.OK.value());
        String jwtToken = resp.extract().body().asString();
        adminToken = jwtToken.substring(1, jwtToken.length() - 1);
    }

    @Test
    public void logoutTest() throws JsonProcessingException {
        LoginDTO loginDTO = new LoginDTO("jacekTeler23", "P@ssw0rd");
        ValidatableResponse response = given()
                .contentType("application/json")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .body(objectMapper.writeValueAsString(loginDTO))
                .when()
                .post(baseUrl + "/auth/authenticate")
                .then()
                .statusCode(HttpStatus.OK.value());

        String jwtToken = response.extract().body().asString();
        String token = jwtToken.substring(1, jwtToken.length() - 1);

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when()
                .post(baseUrl + "/auth/logout")
                .then()
                .statusCode(HttpStatus.OK.value());

    }

}