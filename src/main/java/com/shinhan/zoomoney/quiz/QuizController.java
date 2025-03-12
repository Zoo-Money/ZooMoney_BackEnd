package com.shinhan.zoomoney.quiz;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;

import jakarta.servlet.http.HttpSession;
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

    private final QuizRepository quizRepository;
    private final MemberRepository memberRepository;
        
    // ✅ 퀴즈 제출 및 정답 여부 저장
    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(@RequestBody QuizSubmitDto quizSubmitDto) {
        boolean isCorrect = quizService.submitQuiz(quizSubmitDto);
        return ResponseEntity.ok().body("{\"isCorrect\": " + isCorrect + "}");
    }
    
}

    

