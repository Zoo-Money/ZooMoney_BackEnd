package com.shinhan.zoomoney.stock.entity;

import com.shinhan.zoomoney.member.MemberEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "StockMoney")
public class StockMoneyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stockmoneyNum;

    @OneToOne
    @JoinColumn(name = "member_num")
    MemberEntity member;
    private int stockmoneyTotal;
}
