package com.shinhan.zoomoney.account;

import java.util.Date;

import com.shinhan.zoomoney.child.ChildEntity;
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
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    private String accountName;
    private int accountGoal;
    private int accountNow;
    private Date accountStart;
    private Date accountEnd;
    private boolean accountStatus;



}



