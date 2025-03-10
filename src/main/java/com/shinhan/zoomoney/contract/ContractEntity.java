package com.shinhan.zoomoney.contract;

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
@Table(name = "Contract")
public class ContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contractNum;

    @ManyToOne
    @JoinColumn(name = "member_num")
    private MemberEntity member;

    private int contractMoney;
    private Date contractDate;
    private boolean contractStatus;
    private Date contractProvide;
    private String contractContent;
    private String contractFilepath;
    private String contractImgpath;
    private String contractExcelpath;
}
