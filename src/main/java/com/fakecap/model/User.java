package com.fakecap.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

}