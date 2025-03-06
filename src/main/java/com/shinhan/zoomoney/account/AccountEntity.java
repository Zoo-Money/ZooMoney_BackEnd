package com.shinhan.zoomoney.account;

import com.shinhan.zoomoney.child.ChildEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

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
