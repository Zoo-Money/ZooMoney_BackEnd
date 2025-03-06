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
// 저금통Dto
public class AccountDto {
    private int account_num;
    private int child_num;
    private String account_name;
    private int account_goal;
    private int account_now;
    private Date account_start;
    private Date account_end;
    private boolean account_status;
}
