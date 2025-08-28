package com.fakecap.dto;

import com.fakecap.ShareRequest;
import com.fakecap.ShareResponse;

import java.math.BigDecimal;

public record ShareResponseDto(String orderId, String ticker, BigDecimal amount, boolean success) {

    public ShareResponseDto(ShareRequest shareRequest, ShareResponse shareResponse, boolean success) {
        this(shareResponse.getOrderId(),
                shareRequest.getTicker(),
                new BigDecimal(shareRequest.getInvestmentAmount()),
                success);
    }

}
