import com.fakecap.dto.BalanceDto;
import com.fakecap.dto.UserDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyString;

@QuarkusTest
public class UserControllerTest {

    @Test
    public void testCreateUserRandomEndpoint() {
        given()
                .when().post("/user/random")
                .then()
                .statusCode(200)
                .body("id", is(not(emptyString())),
                        "name", is(not(emptyString())),
                        "email", is(not(emptyString())),
                        "balance", is(0),
                        "shares", is(empty()));
    }

    @Test
    public void testGetUserEndpoint() {

        UserDto user = createUser();

        given()
                .when().get("/user/{userId}", user.id())
                .then()
                .statusCode(200)
                .body("id", is(not(emptyString())),
                        "name", is(not(emptyString())),
                        "email", is(not(emptyString())),
                        "balance", is(0),
                        "shares", is(empty()));
    }

    @Test
    public void testAddBalanceEndpoint() {

        UserDto user = createUser();
        BalanceDto balanceDto = new BalanceDto(new BigDecimal(1000));

        given()
                .contentType(ContentType.JSON)
                .body(balanceDto)
                .when().post("/user/{userId}/balance", user.id())
                .then()
                .statusCode(200)
                .body("id", is(not(emptyString())),
                        "name", is(not(emptyString())),
                        "email", is(not(emptyString())),
                        "balance", is(notNullValue()),
                        "balance", is(balanceDto.amount().intValue()),
                        "shares", is(empty()));
    }

    private static UserDto createUser() {
        return given()
                .when().post("/user/random")
                .then()
                .extract()
                .as(UserDto.class);
    }

}
