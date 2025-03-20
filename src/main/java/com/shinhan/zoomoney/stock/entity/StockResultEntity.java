package com.shinhan.zoomoney.stock.entity;

import java.util.Date;

import com.shinhan.zoomoney.member.MemberEntity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "StockResult")
public class StockResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resultNum;

    @ManyToOne
    @JoinColumn(name = "member_num")
    private MemberEntity member;

    private Date resultDate;
    private double resultRate;
    private int resultRank;
}
