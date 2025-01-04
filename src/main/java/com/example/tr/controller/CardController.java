package com.example.tr.controller;

import com.example.tr.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tr")
public class CardController {

    @Autowired
    private CardService cardService;

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
        String refId = cardService.registerCard(cardNumber, userId);
        return ResponseEntity.ok(refId);
    }
}
