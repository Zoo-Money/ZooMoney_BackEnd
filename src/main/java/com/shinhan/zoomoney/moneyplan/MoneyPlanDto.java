package com.shinhan.zoomoney.moneyplan;

import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MoneyPlanDto {

    private int plan_num;
    private int child_num;
    private Date plan_date;
    private int plan_money;
    private boolean plan_status;
}
