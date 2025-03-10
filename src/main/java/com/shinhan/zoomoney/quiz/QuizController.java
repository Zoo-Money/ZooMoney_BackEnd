package com.shinhan.zoomoney.quiz;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    // ✅ AI 퀴즈 생성 (DB 저장 X)
    @PostMapping("/generate")
    public ResponseEntity<QuizResponseDto> generateQuiz() {
        QuizResponseDto quiz = quizService.generateFinancialQuiz();
        if (quiz == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(quiz);
    }

//    // ✅ 정답 제출 → 정답 여부를 DB에 저장하고 응답 반환
//    @PostMapping("/submit")
//    public ResponseEntity<Map<String, Object>> submitAnswer(@RequestParam int memberNum,
//                                                            @RequestParam boolean userAnswer,
//                                                            @RequestParam String correctAnswer) {
//        boolean isCorrect = quizService.submitAnswer(memberNum, userAnswer, correctAnswer);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("isCorrect", isCorrect);
//
//        return ResponseEntity.ok(response);
//    }
}

