package com.example.tr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentRequest {
    private final String refId;
    private final String tokenValue;
    private final double amount;
}

