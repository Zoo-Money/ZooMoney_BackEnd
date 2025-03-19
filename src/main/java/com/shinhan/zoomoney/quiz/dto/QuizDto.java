package com.shinhan.zoomoney.quiz.dto;

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

}
