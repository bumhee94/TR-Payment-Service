package com.example.tr.dto;

import com.example.tr.validation.ValidAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentRequest {
    private final String refId;
    private final String tokenValue;
    @ValidAmount //0원 및 음수금액 유효성검사 어노테이션
    private final double amount;
    private final String userId;
}

