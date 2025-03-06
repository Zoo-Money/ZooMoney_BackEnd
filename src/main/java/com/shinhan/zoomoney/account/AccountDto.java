package com.shinhan.zoomoney.account;

import lombok.*;

import java.sql.Date;

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
