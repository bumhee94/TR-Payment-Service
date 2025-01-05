package com.example.tr.controller;

import com.example.tr.dto.PaymentRequest;
import com.example.tr.dto.PaymentResponse;
import com.example.tr.service.CardService;
import com.example.tr.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tr")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private PaymentService paymentService;
    /**
     * 카드 등록 API
     *
     * @param cardNumber 암호화된 카드 번호
     * @param userId 사용자 식별키 (CI)
     * @return 등록된 참조 ID
     */
    @PostMapping("/register-card")
    public ResponseEntity<String> registerCard(
            @RequestParam String cardNumber,
            @RequestParam String userId) {
        // 업무 요구사항 1. 사용자의 CI는 이미 습득을 했으며 사용자 식별키로 CI를 사용한다.
        // 따라서 userId는 임의
        String refId = cardService.registerCard(cardNumber, userId);
        return ResponseEntity.ok(refId);
    }

    @PostMapping("/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest request) {
        return paymentService.processPayment(request);
    }
}
