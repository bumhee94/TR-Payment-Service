package com.example.tr.service;

import com.example.tr.dto.CardRegisterRequest;
import com.example.tr.entity.Card;
import com.example.tr.exception.BusinessException;
import com.example.tr.repository.CardRepository;
import com.example.tr.util.EncryptionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ExternalApiService externalApiService;
    private final EncryptionUtil encryptionUtil;

    /**
     * 카드 등록 서비스
     *
     * @param request 카드 등록 요청 데이터
     * @return 참조 ID
     */
    @Transactional
    public String registerCard(CardRegisterRequest request) {
        log.info("[CardService][registerCard] - 카드 등록 요청 처리 시작: userId={}", request.getUserId());

        // 카드 번호 암호화
        String encryptedCardNumber = encryptionUtil.encrypt(request.getCardNumber());
        log.debug("[CardService][registerCard] - 카드 번호 암호화 완료");

        // 중복 카드 번호 검증
        if (cardRepository.existsByCardNumber(encryptedCardNumber)) {
            log.warn("[CardService][registerCard] - 중복된 카드 번호 발견");
            throw new BusinessException("이미 등록된 카드입니다.");
        }

        // TSP에 참조 ID 요청
        String refId = externalApiService.postToTspRefId(encryptedCardNumber);
        log.debug("[CardService][registerCard] - TSP로부터 참조 ID 수신: refId={}", refId);

        // 중복 참조 ID 검증
        if (cardRepository.existsByRefId(refId)) {
            log.warn("[CardService][registerCard] - 중복된 참조 ID 발견");
            throw new BusinessException("이미 존재하는 참조 ID입니다.");
        }

        // 카드 엔티티 생성 및 저장
        Card card = Card.builder()
                .cardNumber(encryptedCardNumber)
                .userId(request.getUserId())
                .refId(refId)
                .build();
        cardRepository.save(card);
        log.info("[CardService][registerCard] - 카드 등록 성공: refId={}", refId);

        return refId;
    }
}
