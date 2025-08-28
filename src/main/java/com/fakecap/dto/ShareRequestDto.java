package com.fakecap.dto;

import java.math.BigDecimal;

public record ShareRequestDto(String ticker, BigDecimal amount) {
}
