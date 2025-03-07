//package com.shinhan.zoomoney.notify;
//
//import java.sql.Timestamp;
//
//import com.shinhan.zoomoney.member.MemberEntity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Entity
//@Table(name = "Notify")
//public class NotifyEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int notifyNum;
//
//    @ManyToOne
//    @JoinColumn(name = "member_num")
//    private MemberEntity member;
//
//    private String notifyContent;
//    private String notifyUrl;
//    private Timestamp notifyTime;
//    private boolean notifyCheck;
//}
