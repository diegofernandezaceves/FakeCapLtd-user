package com.fakecap.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Share {

    private String ticker;
    private BigDecimal amount;

}