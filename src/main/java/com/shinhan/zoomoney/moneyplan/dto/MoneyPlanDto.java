package com.shinhan.zoomoney.moneyplan.dto;

import java.util.Date;
import java.util.List;

import lombok.*;

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

    private List<PlanDetailDto> planDetails;
}
