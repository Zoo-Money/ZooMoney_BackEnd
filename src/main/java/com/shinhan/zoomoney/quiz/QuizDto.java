package com.shinhan.zoomoney.quiz;

import lombok.*;

import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class QuizDto {

    private int quiz_num;
    private int member_num;
    private boolean quiz_check;
    private Date quiz_date;
    
    // 프론트로 퀴즈 데이터를 넘겨주기 위한 DTO
    private String question;
    private String answer;
    private String explanation;
}
