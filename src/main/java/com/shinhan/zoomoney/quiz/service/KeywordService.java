package com.shinhan.zoomoney.quiz.service;

import org.springframework.stereotype.Service;

import com.shinhan.zoomoney.quiz.dto.KeywordDto;
import com.shinhan.zoomoney.quiz.entity.KeywordEntity;
import com.shinhan.zoomoney.quiz.repository.KeywordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    // 키워드 저장(데이터 DB 입력)
    public KeywordEntity saveKeyword(KeywordDto keywordDto) {
        // DTO → Entity 변환
        KeywordEntity keywordEntity = KeywordEntity.builder()
                .keywordWord(keywordDto.getKeywordWord())
                .build();

        return keywordRepository.save(keywordEntity);
    }
}
