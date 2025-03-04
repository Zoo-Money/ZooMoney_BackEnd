package com.shinhan.zoomoney.daily;

import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DailyDto {
    private int daily_num;
    private int child_num;
    private boolean daily_check;
    private Date daily_date;
}
