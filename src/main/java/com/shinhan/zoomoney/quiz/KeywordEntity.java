package com.shinhan.zoomoney.quiz;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "QuizKeyword")
public class KeywordEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int keywordNum; // 키워드 번호

    @Column(nullable = false, length = 100)
    private String keywordWord; // 키워드 이름
}
