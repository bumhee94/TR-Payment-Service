package com.example.tr.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackLoggerTest {
    private static final Logger logger = LoggerFactory.getLogger(SlackLoggerTest.class);

    public static void main(String[] args) {
        logger.info("Slack 로그 전송 테스트: 정상 작동 확인");
        logger.error("Slack 로그 전송 테스트: 에러 발생!");
    }
}
