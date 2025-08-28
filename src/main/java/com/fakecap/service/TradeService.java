package com.fakecap.service;

import com.fakecap.OrderStatus;
import com.fakecap.ShareRequest;
import com.fakecap.ShareResponse;
import com.fakecap.TradeServiceGrpc;
import com.fakecap.dto.ShareRequestDto;
import com.fakecap.dto.ShareResponseDto;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.extern.java.Log;


@Log
@ApplicationScoped
public class TradeService {

    private final TradeServiceGrpc tradeServiceGrpc;

    public TradeService(@GrpcClient("trade-service-grpc") TradeServiceGrpc tradeServiceGrpc) {
        this.tradeServiceGrpc = tradeServiceGrpc;
    }

    public Uni<ShareResponseDto> buyOrSellShare(String userId, ShareRequestDto shareRequestDto) {

        ShareRequest shareRequest = ShareRequest.newBuilder()
                .setUserId(userId)
                .setTicker(shareRequestDto.ticker())
                .setInvestmentAmount(shareRequestDto.amount().intValue())
                .build();

        return this.tradeServiceGrpc.submit(shareRequest)
                .map(shareResponse -> processResponse(shareRequest, shareResponse))
                .invoke(shareResponseDto -> logResponseDto(userId, shareResponseDto))
                .onFailure().recoverWithItem(Unchecked.function(throwable -> {
                    throw new InternalServerErrorException("Failed to process stock request: " + throwable.getMessage());
                }));
    }

    private ShareResponseDto processResponse(ShareRequest request, ShareResponse response) {
        boolean success = OrderStatus.SUCCESS.equals(response.getStatus());
        return new ShareResponseDto(request, response, success);
    }

    private void logResponseDto(String userId, ShareResponseDto shareResponseDto) {
        if (shareResponseDto.success()) {
            log.info("Trade successful with orderId:" + shareResponseDto.orderId() + " for userId: " + userId + ", ticker: " + shareResponseDto.ticker() + ", amount: " + shareResponseDto.amount());
        } else {
            log.warning("Trade failed for userId: " + userId + ", ticker: " + shareResponseDto.ticker() + ", amount: " + shareResponseDto.amount());
        }
    }
}

