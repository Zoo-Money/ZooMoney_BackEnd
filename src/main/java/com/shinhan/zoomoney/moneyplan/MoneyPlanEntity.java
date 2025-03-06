package com.shinhan.zoomoney.moneyplan;

import java.util.Date;

import com.shinhan.zoomoney.child.ChildEntity;
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
@Table(name="MoneyPlan")
public class MoneyPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int planNum;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    private Date planDate;
    private int planMoney;
    private boolean planStatus;

}
