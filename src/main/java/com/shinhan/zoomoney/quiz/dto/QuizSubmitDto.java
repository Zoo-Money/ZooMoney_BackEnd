package com.shinhan.zoomoney.quiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizSubmitDto {
    private int memberNum;
    private int quizNum;
    private String correctAnswer; // AI 정답
    private String userAnswer;
}
