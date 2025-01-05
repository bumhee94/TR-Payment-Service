package com.example.tr.service;

import com.example.tr.dto.PaymentRequest;
import com.example.tr.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    @Autowired
    private ExternalApiService externalApiService;

    public PaymentResponse processPayment(PaymentRequest request) {
        // 1. TSP로 토큰 요청
        String tokenValue = externalApiService.postToTspToken(request.getRefId());

        // 업무 요구사항3. 결제과정에서는 카드정보를 사용하지 않는다. >> refId로 얻은 1회용 토큰으로만
        // 2. ISSUER로 결제 요청
        // TSP에서 받은 토큰과 결제정보(request)
        PaymentResponse response = externalApiService.postToIssuer(request, tokenValue);
        return response;
    }
}
