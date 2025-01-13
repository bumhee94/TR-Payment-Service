package com.example.tr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cardNumber; // 암호화된 카드 번호

    @Column(nullable = false)
    private String userId; // 사용자 식별키 (CI)

    @Column(nullable = false, unique = true)
    private String refId; // 카드 참조 ID (TSP와 통신 후 생성)

    @Column(nullable = false, updatable = false)
    private String createId; // 등록자 ID

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDt; // 등록일

    @Column(nullable = true)
    private String updateId; // 수정자 ID

    @Column(nullable = true)
    private LocalDateTime updateDt; // 수정일
}
