package com.shinhan.zoomoney.account;

import java.sql.Date;

import com.shinhan.zoomoney.child.ChildEntity;

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
@Table(name = "Account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int account_num;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    private String account_name;
    private int account_goal;
    private int account_now;
    private Date account_start;
    private Date account_end;
    private boolean account_status;
}
