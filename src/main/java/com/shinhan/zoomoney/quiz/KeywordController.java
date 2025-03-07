package com.shinhan.zoomoney.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz/keyword")
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    // 키워드 데이터 DB 입력 API (POST 요청)
    @PostMapping
    public ResponseEntity<KeywordEntity> saveKeyword(@RequestBody KeywordDto keywordDto) {
        KeywordEntity savedKeyword = keywordService.saveKeyword(keywordDto);
        return ResponseEntity.ok(savedKeyword);
    }
}
