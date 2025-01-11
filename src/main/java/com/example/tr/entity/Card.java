package com.example.tr.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
