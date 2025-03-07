package com.shinhan.zoomoney.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

	private final QuizService quizService;

	// ✅ 랜덤 키워드 2개를 골라서 금융 퀴즈를 생성하는 API
	@PostMapping("/generate")
	public String generateQuiz() {
		return quizService.generateFinancialQuiz();
	}
}
