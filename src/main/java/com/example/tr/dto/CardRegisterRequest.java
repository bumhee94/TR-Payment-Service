package com.example.tr.dto;

import com.example.tr.validation.ValidCardNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class CardRegisterRequest {
    @ValidCardNumber
    private String cardNumber; // 카드 번호
    private String userId;     // 사용자 ID
}
