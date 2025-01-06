package com.example.tr.controller;

import com.example.tr.dto.CardRegisterRequest;
import com.example.tr.dto.PaymentRequest;
import com.example.tr.dto.PaymentResponse;
import com.example.tr.service.CardService;
import com.example.tr.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/tr")
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private PaymentService paymentService;

    /**
     * 카드 등록 API
     */
    @PostMapping("/register-card")
    public ResponseEntity<String> registerCard(@RequestBody CardRegisterRequest request) {
        try{
            String refId = cardService.registerCard(request.getCardNumber(), request.getUserId());
            return ResponseEntity.ok("카드 등록 성공! 참조 ID: " + refId);
        }catch (RuntimeException e){
            log.error("카드 등록 요청 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    /**
     * 결제 요청 API
     */
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest request) {
        try{
            PaymentResponse response = paymentService.processPayment(request);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            log.error("결제 실패: {}", e.getMessage());
            PaymentResponse errorResponse = new PaymentResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
