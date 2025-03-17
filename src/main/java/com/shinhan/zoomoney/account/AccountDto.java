package com.shinhan.zoomoney.account;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AccountDto {
    private int accountNum;
    private int memberNum;
    private String accountName;
    private int accountGoal;
    private int accountNow;
    private Date accountStart;
    private Date accountEnd;
    private boolean accountStatus;
    private boolean accountRequest;
}
