package com.shinhan.zoomoney.quiz;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class KeywordDto {
    
    private int keywordNum; // 키워드 번호
    private String keywordWord; // 키워드 이름
}
