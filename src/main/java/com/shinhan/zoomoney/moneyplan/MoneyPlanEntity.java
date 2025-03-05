package com.shinhan.zoomoney.moneyplan;

import com.shinhan.zoomoney.child.ChildEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="MoneyPlan")
public class MoneyPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int plan_num;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    private Date plan_date;
    private int plan_money;
    private boolean plan_status;

}
