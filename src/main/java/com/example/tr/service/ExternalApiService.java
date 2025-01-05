package com.example.tr.service;

import com.example.tr.dto.PaymentRequest;
import com.example.tr.dto.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class ExternalApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String TSP_REF_URL = "http://localhost:8081/api/tsp/generate-ref-id";
    private static final String TSP_TOKEN_URL = "http://localhost:8081/api/tsp/generate-token";
    private static final String ISSUER_URL = "http://localhost:8082/api/issuer/process-payment";

    /**
     * TSP로 POST 요청을 보내고 응답받기 (참조 ID 생성)
     *
     * @param encryptCardNumber 암호화된 카드 번호
     * @return TSP에서 생성된 참조 ID
     */
    public String postToTspRefId(String encryptCardNumber) {
        log.info("TSP로 참조 ID 요청 시작: Payload={}", encryptCardNumber);

        try {
            String response = restTemplate.postForObject(TSP_REF_URL, encryptCardNumber, String.class);
            log.info("TSP로부터 참조 ID 응답 수신: Response={}", response);
            return response;
        } catch (HttpClientErrorException e) {
            log.error("TSP 참조 ID 요청 실패: StatusCode={}, Message={}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("TSP 참조 ID 요청 중 오류 발생", e);
        }
    }

    /**
     * TSP로 POST 요청을 보내고 응답받기 (토큰 생성)
     *
     * @param refId TR에서 생성된 참조 ID
     * @return TSP에서 생성된 토큰 값
     */
    public String postToTspToken(String refId) {
        log.info("TSP로 토큰 요청 시작: Payload={}", refId);

        try {
            String response = restTemplate.postForObject(TSP_TOKEN_URL, refId, String.class);
            log.info("TSP로부터 토큰 응답 수신: Response={}", response);
            return response;
        } catch (HttpClientErrorException e) {
            log.error("TSP 토큰 요청 실패: StatusCode={}, Message={}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("TSP 토큰 요청 중 오류 발생", e);
        }
    }

    /**
     * Issuer로 POST 요청을 보내고 응답받기 (결제 처리)
     *
     * @param request PaymentRequest 객체
     * @param tokenValue TSP에서 생성된 토큰 값
     * @return Issuer로부터의 결제 응답
     */
    public PaymentResponse postToIssuer(PaymentRequest request, String tokenValue) {
        log.info("Issuer로 결제 요청 시작: TokenValue={}, Payload={}", tokenValue, request);

        try {
            PaymentResponse response = restTemplate.postForObject(
                    ISSUER_URL,
                    new PaymentRequest(request.getAmount(), tokenValue),
                    PaymentResponse.class
            );
            log.info("Issuer로부터 결제 응답 수신: Response={}", response);
            return response;
        } catch (HttpClientErrorException e) {
            log.error("Issuer 결제 요청 실패: StatusCode={}, Message={}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Issuer 결제 요청 중 오류 발생", e);
        }
    }
}
