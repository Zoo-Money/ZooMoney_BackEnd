package com.shinhan.zoomoney.member;

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
@Table(name="Member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberNum;

    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberPhone;
    private String memberType;
    private int memberParent;
    private int memberPoint;
    private String memberAccount;

}
