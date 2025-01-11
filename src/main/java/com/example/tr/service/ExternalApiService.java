package com.example.tr.service;

import com.example.tr.dto.PaymentRequest;
import com.example.tr.dto.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ExternalApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String TSP_REF_URL = "http://localhost:8081/api/tsp/generate-ref-id";
    private static final String TSP_TOKEN_URL = "http://localhost:8081/api/tsp/generate-token";
    private static final String ISSUER_URL = "http://localhost:8082/api/issuer/process-payment";

    public String postToTspRefId(String encryptCardNumber) {
        log.info("[TR][ExternalApiService][postToTspRefId] - TSP로 참조 ID 요청 시작");
        String response = restTemplate.postForObject(TSP_REF_URL, encryptCardNumber, String.class);
        log.info("[TR][ExternalApiService][postToTspRefId] - TSP로부터 참조 ID 응답 수신: response={}", response);
        return response;
    }

    public String postToTspToken(String refId) {
        log.info("[TR][ExternalApiService][postToTspToken] - TSP로 토큰 요청 시작: refId={}", refId);
        String response = restTemplate.postForObject(TSP_TOKEN_URL, refId, String.class);
        log.info("[TR][ExternalApiService][postToTspToken] - TSP로부터 토큰 응답 수신: response={}", response);
        return response;
    }

    public PaymentResponse postToIssuer(PaymentRequest request, String tokenValue) {
        log.info("[TR][ExternalApiService][postToIssuer] - Issuer로 결제 요청 시작: TokenValue={}, Payload={}", tokenValue, request);
        PaymentResponse response = restTemplate.postForObject(
                ISSUER_URL,
                new PaymentRequest(request.getRefId(), tokenValue, request.getAmount()),
                PaymentResponse.class
        );
        log.info("[TR][ExternalApiService][postToIssuer] - Issuer로부터 결제 응답 수신: response={}", response);
        return response;
    }
}
