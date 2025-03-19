package com.shinhan.zoomoney.quiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizResponseDto {
    private String question;
    private String answer;
    private String explanation;
}
