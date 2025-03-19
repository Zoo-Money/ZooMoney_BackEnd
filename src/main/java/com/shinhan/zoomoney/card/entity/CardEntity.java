package com.shinhan.zoomoney.card.entity;

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
@Table(name = "Card")
public class CardEntity {
    @Id
    private String cardNum;

    @OneToOne
    @JoinColumn(name = "member_num")
    private MemberEntity member;

    private String cardMetadata;
    private int cardMoney;
    private Date cardUpdate;

    @PrePersist
    public void setCardUpdate() {
        this.cardUpdate = new Date(); // 현재 날짜와 시간을 자동으로 설정
    }
}
