package com.example.tr.controller;

import com.example.tr.dto.CardRegisterRequest;
import com.example.tr.dto.PaymentRequest;
import com.example.tr.dto.PaymentResponse;
import com.example.tr.service.CardService;
import com.example.tr.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/tr")
public class CardController {

    private final CardService cardService;
    private final PaymentService paymentService;

    /**
     * 카드 등록 API
     */
    @PostMapping("/register-card")
    public ResponseEntity<String> registerCard(@Valid @RequestBody CardRegisterRequest request) {
        log.info("[CardController][registerCard] - 카드 등록 요청 수신: userId={}", request.getUserId());
        String refId = cardService.registerCard(request);
        log.info("[CardController][registerCard] - 카드 등록 성공: refId={}", refId);
        return ResponseEntity.ok("카드 등록 성공! 참조 ID: " + refId);
    }

    /**
     * 결제 요청 API
     */
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> pay(@Valid @RequestBody PaymentRequest request) {
        log.info("[CardController][pay] - 결제 요청 수신: refId={}, amount={}", request.getRefId(), request.getAmount());
        PaymentResponse response = paymentService.processPayment(request);
        log.info("[CardController][pay] - 결제 요청 처리 완료: response={}", response);
        return ResponseEntity.ok(response);
    }
}
