package com.shinhan.zoomoney.account;

import java.sql.Date;
import java.time.LocalDate;

import com.shinhan.zoomoney.member.MemberEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
@Table(name = "Account")
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
    private boolean accountRequest;

    // Entity 기본값 설정
    @PrePersist
    public void prePersist() {
        this.accountNow = 0;
        this.accountStart = Date.valueOf(LocalDate.now());  // 현재 날짜
        this.accountStatus = true;  // 기본값 true
        this.accountRequest = false;  // 기본값 false
    }
}