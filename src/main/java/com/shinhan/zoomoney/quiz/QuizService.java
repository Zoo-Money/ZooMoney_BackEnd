package com.shinhan.zoomoney.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final KeywordRepository keywordRepository;
    private final QuizRepository quizRepository;
    private final RestTemplate restTemplate; // RestTemplate Bean으로 등록 필요!

    // ✅ Postman에서 사용한 URL 그대로 사용 (API_KEY 포함)
    private static final String GEMINI_API_URL = 
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyDHayHZUvvzzIDA7gOYjIn4VIsZKQ5D9dE";

    public String generateFinancialQuiz() {
        // ✅ 1️⃣ 랜덤 키워드 2개 가져오기
        List<KeywordEntity> keywords = keywordRepository.findRandomKeywords();
        if (keywords.size() < 2) {
            return "키워드가 부족합니다.";
        }

        String keyword1 = keywords.get(0).getKeywordWord();
        String keyword2 = keywords.get(1).getKeywordWord();
        
        // ✅ 2️⃣ Gemini API에 보낼 프롬프트 생성
        String prompt = String.format(
            "'%s'와 '%s' 이 키워드와 관련된 새로운 금융 OX 퀴즈를 만들어줘. "
            + "중학색 수준으로 간단히 만들어줘. "
            + "JSON 형식으로 한국어로 응답해줘. "
            + "예제: { 'question': '질문 내용', 'answer': 'O 또는 X', 'explanation': '정답에 대한 간단한 설명' }",
            keyword1, keyword2
        );

        // ✅ 3️⃣ Gemini API 호출
        String quizQuestion = callGeminiApi(prompt);

        // ✅ 4️⃣ 퀴즈 저장 (child 연동 없음)
        QuizEntity quiz = QuizEntity.builder()
                .quizCheck(false)
                .quizDate(new Date())
                .build();
        quizRepository.save(quiz);

        return quizQuestion;
    }

    private String callGeminiApi(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // ✅ Postman에서 요청한 JSON 구조 그대로 사용
            String requestBody = String.format(
                "{ \"contents\": [{ \"parts\": [{ \"text\": \"%s\" }] }], \"generationConfig\": { \"temperature\": 2.0, \"top_k\": 40, \"top_p\": 0.8 } }",
                prompt
            );

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    GEMINI_API_URL, // ✅ Postman에서 사용한 URL 그대로 사용
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // ✅ 응답 JSON 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText(); 

        } catch (Exception e) {
            e.printStackTrace();
            return "퀴즈 생성 중 오류 발생";
        }
    }
}
