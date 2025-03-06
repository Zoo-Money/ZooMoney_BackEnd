package com.shinhan.zoomoney.card;


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
@Table(name="Card")
public class CardEntity {
    @Id
    private String cardNum;

    @ManyToOne
    @JoinColumn(name = "member_num")
    private MemberEntity member;

    private String cardMetadata;
    private int cardMoney;
}
