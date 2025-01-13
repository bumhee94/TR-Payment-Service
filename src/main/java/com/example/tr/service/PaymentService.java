package com.example.tr.service;

import com.example.tr.dto.PaymentRequest;
import com.example.tr.dto.PaymentResponse;
import com.example.tr.exception.BusinessException;
import com.example.tr.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final ExternalApiService externalApiService;
    private final CardRepository cardRepository;

    /**
     * 결제 요청 처리
     *
     * @param request 결제 요청 데이터
     * @return 결제 응답 데이터
     */
    public PaymentResponse processPayment(PaymentRequest request) {
        log.info("[PaymentService][processPayment] - 결제 요청 처리 시작: refId={}, amount={}, userId={}", request.getRefId(), request.getAmount(), request.getUserId());

        // 1. refId 유효성 검증
        if (!cardRepository.existsByRefId(request.getRefId())) {
            log.warn("[PaymentService][processPayment] - 유효하지 않은 refId: {}", request.getRefId());
            throw new BusinessException("유효하지 않은 참조 ID입니다: " + request.getRefId());
        }

        // 2. TSP로 토큰 요청
        String tokenValue = externalApiService.postToTspToken(request.getRefId());
        log.debug("[PaymentService][processPayment] - TSP로부터 토큰 수신: tokenValue={}", tokenValue);

        // 3. ISSUER로 결제 요청
        PaymentResponse response = externalApiService.postToIssuer(request, tokenValue);
        log.info("[PaymentService][processPayment] - 결제 요청 완료: response={}", response);

        return response;
    }
}
