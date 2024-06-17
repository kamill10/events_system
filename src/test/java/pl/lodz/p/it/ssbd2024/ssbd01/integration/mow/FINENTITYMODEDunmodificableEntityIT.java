package pl.lodz.p.it.ssbd2024.ssbd01.integration.mow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AbstractControllerIT;

import java.time.LocalDateTime;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class FINENTITYMODEDunmodificableEntityIT extends AbstractControllerIT {
    private final Random random = new Random();
    @BeforeEach
    public void auth() throws JsonProcessingException {
        authenticationToManagerTest();
    }
    @Test
    public void modifyEventAfterModificationTimeRange() {
        //the entity is unmodifiable because the modification time range has passed(event ended)
        String getEventResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + managerToken)
                .header("Accept-Language", "pl-PL")
                .when()
                .get(baseUrl + "/events/5b9cd583-b097-40a6-887f-7d4f440f99c9")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .header(HttpHeaders.ETAG);
        String etag = getEventResponse.substring(1, getEventResponse.length() - 1);
        UpdateEventDTO eventDTO = new UpdateEventDTO(
                RandomStringUtils.random(3, true, false),
                RandomStringUtils.random(3, true, false),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(random.nextInt(10))
        );
        given()
                .contentType("application/json")
                .header("Accept-Language", "pl-PL")
                .header("Authorization", "Bearer " + managerToken)
                .header(HttpHeaders.IF_MATCH, etag)
                .body(eventDTO)
                .when()
                .put(baseUrl + "/events/5b9cd583-b097-40a6-887f-7d4f440f99c9")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
