package pl.lodz.p.it.ssbd2024.ssbd01.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import pl.lodz.p.it.ssbd2024.ssbd01.integration.AccountControllerIT;

import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

@Testcontainers
public class AuthenticationControllerIT {

    static Properties properties = new Properties();

    static Network network = Network.newNetwork();

    @BeforeAll
    public static void setup() throws IOException {
        System.setProperty("spring.profiles.active", "test");
        properties.load(AccountControllerIT.class.getClassLoader().getResourceAsStream("test.properties"));
    }

    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Container
    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName(properties.getProperty("jdbc.database"))
            .withUsername(properties.getProperty("jdbc.admin.user"))
            .withPassword(properties.getProperty("jdbc.admin.password"))
            .withExposedPorts(5432)
            .withNetworkAliases("postgres")
            .withNetwork(network)
            .withInitScript("sql/create-users.sql")
            .waitingFor(Wait.forSuccessfulCommand("pg_isready -U " + properties.getProperty("jdbc.admin.user")))
            .withReuse(true);

    @Container
    GenericContainer<?> tomcat = new GenericContainer<>("tomcat:10.1.19-jre21")
            .withExposedPorts(8080)
            .withNetworkAliases("tomcat")
            .withNetwork(network)
            .dependsOn(postgres)
            .withCopyFileToContainer(
                    MountableFile.forHostPath("target/ssbd01.war"),
                    "/usr/local/tomcat/webapps/ssbd01.war"
            )
            .withReuse(true);

    @Test
    public void test() {
        int port = tomcat.getMappedPort(8080);
        given()
                .when()
                .get("http://localhost:" + port + "/ssbd01/api/accounts")
                .then()
                .statusCode(403);
    }

}
