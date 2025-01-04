package com.example.tr.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * TSP로 POST 요청을 보내고 응답받기
     *
     * @param url TSP의 API URL
     * @param cardNumber 암호화된 카드 번호
     * @return TSP에서 생성된 참조 ID
     */
    public String postToTsp(String url, String cardNumber) {
        return restTemplate.postForObject(url, cardNumber, String.class);
    }
}
