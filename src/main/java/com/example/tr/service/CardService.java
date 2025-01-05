package com.example.tr.service;

import com.example.tr.entity.Card;
import com.example.tr.repository.CardRepository;
import com.example.tr.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ExternalApiService externalApiService;

    @Autowired
    private EncryptionUtil encryptionUtil;
    /**
     * 카드 등록 서비스
     *
     * @param cardNumber 암호화된 카드 번호
     * @param userId 사용자 식별키 (CI)
     * @return 참조 ID
     */
    public String registerCard(String cardNumber, String userId) {

        //카드정보 AES 암호화
        String encryptCardNumber = encryptionUtil.encrypt(cardNumber);

        // 암호화 카드번호 중복 체크
        if (cardRepository.existsByCardNumber(encryptCardNumber)) {
            throw new RuntimeException("이미 등록된 카드입니다.");
        }

        // 업무 요구사항 2. 카드정보는 페이 결제사에서 볼 수 없으며 암호화하여 토큰 관리사에 전달한다.
        // 외부 인터페이스(토큰관리사/TSP)를 통해 참조 ID 생성 요청
        // 암호화된 카드정보 TSP에 전달
        String refId = externalApiService.postToTspRefId(encryptCardNumber);

        // 참조 ID 검증
        if (cardRepository.existsByRefId(refId)) {
            throw new RuntimeException("이미 존재하는 참조 ID입니다. 관리자에게 문의주세요.");
        }

        // Card 엔티티 생성 및 저장
        Card card = new Card();
        card.setCardNumber(cardNumber); //암호화된 카드번호 저장 (업무 요구사항2)
        card.setUserId(userId);
        card.setRefId(refId); //TSP에서 넘어온 참조아이디

        cardRepository.save(card);

        return refId;
    }
}
