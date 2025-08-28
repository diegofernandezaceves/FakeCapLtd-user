import com.fakecap.OrderStatus;
import com.fakecap.ShareRequest;
import com.fakecap.ShareResponse;
import com.fakecap.TradeServiceGrpc;
import com.fakecap.dto.ShareRequestDto;
import com.fakecap.dto.UserDto;
import io.grpc.internal.GrpcUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.operators.uni.builders.UniCreateFromKnownItem;
import io.vertx.grpc.server.GrpcServerResponse;
import jakarta.inject.Inject;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class TradeControllerTest {

    @Inject
    Faker faker;

    @InjectMock
    @GrpcClient("trade-service-grpc")
    TradeServiceGrpc tradeServiceGrpc;

    @Test
    @DisplayName("User trade is successful")
    public void givenShareRequestWhenTradeIsSuccessfulThenOrderIdReturned() {

        UserDto user = createUser();
        ShareRequestDto shareRequestDto = new ShareRequestDto("fake", new BigDecimal(faker.number().positive()));

        ShareResponse shareResponse = ShareResponse.newBuilder()
                .setOrderId(faker.internet().uuidv4())
                .setStatus(OrderStatus.SUCCESS)
                .build();

        when(tradeServiceGrpc.submit(any(ShareRequest.class))).thenReturn(new UniCreateFromKnownItem<>(shareResponse));

        given()
                .contentType(ContentType.JSON)
                .body(shareRequestDto)
                .when().post("/trade/{userId}", user.id())
                .then()
                .statusCode(OK.code())
                .body("orderId", is(shareResponse.getOrderId()),
                        "ticker", is(shareRequestDto.ticker()),
                        "amount", is(shareRequestDto.amount().intValue()));
    }

    @Test
    @DisplayName("User trade is not successful")
    public void givenShareRequestWhenTradeIsNotSuccessfulThenExceptionIsThrow() {

        UserDto user = createUser();
        ShareRequestDto shareRequestDto = new ShareRequestDto("fake", new BigDecimal(faker.number().positive()));

        ShareResponse shareResponse = ShareResponse.newBuilder()
                .setOrderId(faker.internet().uuidv4())
                .setStatus(OrderStatus.FAILURE)
                .build();

        when(tradeServiceGrpc.submit(any(ShareRequest.class))).thenReturn(new UniCreateFromKnownItem<>(shareResponse));

        given()
                .contentType(ContentType.JSON)
                .body(shareRequestDto)
                .when().post("/trade/{userId}", user.id())
                .then()
                .statusCode(OK.code());
    }

    @Test
    @DisplayName("User trade fails due to gRPC service failure")
    public void givenShareRequestWhenTradeFailsThenExceptionIsThrow() {

        UserDto user = createUser();
        ShareRequestDto shareRequestDto = new ShareRequestDto("fake", new BigDecimal(faker.number().positive()));

        when(tradeServiceGrpc.submit(any(ShareRequest.class))).thenThrow(new RuntimeException("gRPC service is down"));

        given()
                .contentType(ContentType.JSON)
                .body(shareRequestDto)
                .when().post("/trade/{userId}", user.id())
                .then()
                .statusCode(INTERNAL_SERVER_ERROR.code());
    }

    private static UserDto createUser() {
        return given()
                .when().post("/user/random")
                .then()
                .extract()
                .as(UserDto.class);
    }

}
