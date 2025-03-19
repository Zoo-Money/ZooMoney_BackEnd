package com.shinhan.zoomoney.quiz.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.zoomoney.quiz.dto.QuizResponseDto;
import com.shinhan.zoomoney.quiz.dto.QuizSubmitDto;
import com.shinhan.zoomoney.quiz.service.QuizService;

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

    // ✅ 퀴즈 제출 및 정답 여부 저장
    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(@RequestBody QuizSubmitDto quizSubmitDto,
            @RequestParam("memberNum") int memberNum) {

        // int memberNum = 8; // 테스트용 고정값

        // ✅ 퀴즈 data가 5개 이상이면 퀴즈 응시 불가능
        // true면 응시 불가
        int quizCount = quizService.howManyQuiz(memberNum);
        if (quizCount >= 5) {
            return ResponseEntity.ok(Collections.singletonMap("isLimited", true));
        }

        // ✅ 퀴즈 제출 및 정답 여부 저장
        boolean isCorrect = quizService.submitQuiz(quizSubmitDto, memberNum);
        return ResponseEntity.ok().body("{\"isCorrect\": " + isCorrect + "}");
    }

    // ✅ 오늘 푼 퀴즈 개수 확인 API
    @GetMapping("/count")
    public ResponseEntity<?> getTodayQuizCount(@RequestParam("memberNum") int memberNum) {
        // int memberNum = 8; // 테스트용 고정값
        int quizCount = quizService.howManyQuiz(memberNum);

        return ResponseEntity.ok(Collections.singletonMap("quizCount", quizCount));
    }

    // ✅ 오늘 맞은 퀴즈 개수 확인 API
    @GetMapping("/total")
    public ResponseEntity<?> getCorrectAnswerCount(@RequestParam("memberNum") int memberNum) {
        // int memberNum = 8; // 테스트용 고정값
        int correctAnswerCount = quizService.howManyCorrectAnswer(memberNum);

        return ResponseEntity.ok(Collections.singletonMap("correctAnswerCount", correctAnswerCount));
    }

    // ✅ 오늘 푼 퀴즈 정답 여부 리스트 가져오기
    @GetMapping("/answerlist")
    public ResponseEntity<?> getTodayAnswerList(@RequestParam("memberNum") int memberNum) {
        // int memberNum = 8; // 테스트용 고정값
        List<Integer> answerList = quizService.todayAnswerList(memberNum);

        return ResponseEntity.ok(Collections.singletonMap("answerList", answerList));

    }

}