package com.example.tr.service;

import com.example.tr.entity.Card;
import com.example.tr.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ExternalApiService externalApiService;

    /**
     * 카드 등록 서비스
     *
     * @param cardNumber 암호화된 카드 번호
     * @param userId 사용자 식별키 (CI)
     * @return 참조 ID
     */
    public String registerCard(String cardNumber, String userId) {
        // 카드번호 중복 체크
        if (cardRepository.existsByCardNumber(cardNumber)) {
            throw new RuntimeException("이미 등록된 카드입니다.");
        }

        // 카드 번호 검증
        if(isValidCardNumber(cardNumber)){
            throw new RuntimeException("유효하지 않은 카드번호 입니다.");
        }

        // 외부 API를 통해 참조 ID 생성 요청
        // TSP(토큰관리사)
        String tspUrl = "http://localhost:8081/api/tsp/generate-ref-id";
        String refId = externalApiService.postToTsp(tspUrl, cardNumber);

        // Card 엔티티 생성 및 저장
        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setUserId(userId);
        card.setRefId(refId);

        cardRepository.save(card);

        return refId;
    }

    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber.matches("\\d{16}");
    }
}
