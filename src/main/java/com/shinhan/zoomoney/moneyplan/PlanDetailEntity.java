package com.shinhan.zoomoney.moneyplan;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="PlanDetail")
public class PlanDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detailNum;

    @ManyToOne
    @JoinColumn(name = "plan_num")
    private MoneyPlanEntity moneyplan;

    @ManyToOne
    @JoinColumn(name = "category_num")
    private CategoryEntity category;

    private int detailMoney;
}
