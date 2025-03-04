package com.shinhan.zoomoney.card;

import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UseHistoryDto {

    private int usehist_num;
    private String card_num;
    private int category_num;
    private int account_num;
    private int usehist_money;
    private String usehist_shop;
    private String usehist_type;
    private Date usehist_date;
    private Timestamp usehist_time;
}
