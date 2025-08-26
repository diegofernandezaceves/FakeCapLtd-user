package com.fakecap.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity
public class User {

    private ObjectId id;
    private String name;
    private String email;
    private BigDecimal balance = BigDecimal.ZERO;
    private List<Share> shares = new ArrayList<>();

    public void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

}