package com.shinhan.zoomoney.stock;

import lombok.*;

import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockHistoryDto {

    private int stockhist_num;
    private int child_num;
    private int stock_num;
    private String stockhist_type;
    private int stockhist_amount;
    private int stockhist_price;
    private Date stockhist_date;
}
