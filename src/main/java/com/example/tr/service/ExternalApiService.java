package com.example.tr.service;

import com.example.tr.dto.PaymentRequest;
import com.example.tr.dto.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ExternalApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String TSP_REF_URL = "http://localhost:8081/api/tsp/generate-ref-id";
    private static final String TSP_TOKEN_URL = "http://localhost:8081/api/tsp/generate-token";
    private static final String ISSUER_URL = "http://localhost:8082/api/issuer/process-payment";

    /**
     * TSP로 POST 요청을 보내고 응답받기 (참조 ID 생성)
     */
    public String postToTspRefId(String encryptCardNumber) {
        log.info("TSP로 참조 ID 요청 시작: Payload={}", encryptCardNumber);

        try {
            String response = restTemplate.postForObject(TSP_REF_URL, encryptCardNumber, String.class);
            log.info("TSP로부터 참조 ID 응답 수신: Response={}", response);
            return response;
        } catch (HttpClientErrorException e) {
            log.error("TSP 참조 ID 요청 실패: StatusCode={}, ResponseBody={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("TSP 요청 중 에러 발생: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("TSP 요청 중 알 수 없는 에러 발생: {}", e.getMessage());
            throw new RuntimeException("TSP와 통신 중 알 수 없는 에러 발생", e);
        }
    }

    /**
     * TSP로 POST 요청을 보내고 응답받기 (토큰 생성)
     */
    public String postToTspToken(String refId) {
        log.info("TSP로 토큰 요청: Payload={}", refId);

        try {
            String response = restTemplate.postForObject(TSP_TOKEN_URL, refId, String.class);
            log.info("TSP로부터 토큰 응답: Response={}", response);
            return response;
        } catch (HttpClientErrorException e) {
            log.error("TSP 토큰 요청 실패: StatusCode={}, Message={}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("TSP 토큰 요청 중 오류 발생: " + e.getResponseBodyAsString());
        }
    }

    /**
     * Issuer로 POST 요청을 보내고 응답받기 (결제 처리)
     */
    public PaymentResponse postToIssuer(PaymentRequest request, String tokenValue) {
        log.info("Issuer로 결제 요청: TokenValue={}, Payload={}", tokenValue, request);

        try {
            PaymentResponse response = restTemplate.postForObject(
                    ISSUER_URL,
                    new PaymentRequest(request.getAmount(), tokenValue),
                    PaymentResponse.class
            );
            log.info("Issuer로부터 결제 응답: Response={}", response);
            return response;
        } catch (HttpClientErrorException e) {
            log.error("Issuer 결제 요청 실패: StatusCode={}, Message={}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Issuer 결제 요청 중 오류 발생: " + e.getResponseBodyAsString());
        }
    }
}
