package com.shinhan.zoomoney.account;

import java.util.Date;

import com.shinhan.zoomoney.member.MemberEntity;

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
@Table(name="Account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountNum;

    @ManyToOne
    @JoinColumn(name = "member_num")
    private MemberEntity member;

    private String accountName;
    private int accountGoal;
    private int accountNow;
    private Date accountStart;
    private Date accountEnd;
    private boolean accountStatus;



}



