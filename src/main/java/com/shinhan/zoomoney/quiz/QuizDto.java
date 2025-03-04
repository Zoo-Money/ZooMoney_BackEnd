package com.shinhan.zoomoney.quiz;

import lombok.*;

import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class QuizDto {

    private int quiz_num;
    private int child_num;
    private boolean quiz_check;
    private Date quiz_date;
}
