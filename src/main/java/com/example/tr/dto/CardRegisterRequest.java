package com.example.tr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRegisterRequest {
    private String cardNumber; // 카드 번호
    private String userId;     // 사용자 ID
}
