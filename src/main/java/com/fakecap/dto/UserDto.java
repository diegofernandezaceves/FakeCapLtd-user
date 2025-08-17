package com.fakecap.dto;

import com.fakecap.model.Share;

import java.math.BigDecimal;
import java.util.List;

public record UserDto(String id, String name, String email, BigDecimal balance, List<Share> shares) {

    public UserDto(String name, String email) {
        this(null, name, email, BigDecimal.ZERO, List.of());
    }

}


