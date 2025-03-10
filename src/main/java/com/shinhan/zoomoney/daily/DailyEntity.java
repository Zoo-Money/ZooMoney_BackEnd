package com.shinhan.zoomoney.daily;

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
@Table(name="Daily")
public class DailyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dailyNum;

    @ManyToOne
    @JoinColumn(name = "member_num")
    private MemberEntity member;

    private boolean dailyCheck;
    private Date dailyDate;
}
