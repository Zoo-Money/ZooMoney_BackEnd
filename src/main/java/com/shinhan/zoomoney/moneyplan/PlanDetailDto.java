package com.shinhan.zoomoney.moneyplan;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PlanDetailDto {

    private int detail_num;
    private int plan_num;
    private int category_num;
    private int detail_money;
}
