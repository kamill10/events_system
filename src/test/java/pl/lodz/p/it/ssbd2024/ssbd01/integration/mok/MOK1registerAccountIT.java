package pl.lodz.p.it.ssbd2024.ssbd01.integration.mok;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.create.CreateAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.LanguageEnum;

import static io.restassured.RestAssured.given;

public class MOK1registerAccountIT extends AbstractControllerIT {

    @BeforeEach
    public void authenticate() throws JsonProcessingException {
        authenticationToAdminTest();
        authenticationToParticipantTest();
        authenticationToManagerTest();
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
}
