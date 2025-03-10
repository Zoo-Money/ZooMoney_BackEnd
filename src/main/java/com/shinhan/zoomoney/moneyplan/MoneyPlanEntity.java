package com.shinhan.zoomoney.moneyplan;

import java.util.Date;

import com.shinhan.zoomoney.member.MemberEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    @JoinColumn(name = "member_num")
    private MemberEntity member;

    private Date planDate;
    private int planMoney;
    private boolean planStatus;

}
