package com.shinhan.zoomoney.stock.dto;

import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockHistoryBackupDto {
    private int stockhist_num;
    private int child_num;
    private int stock_num;
    private String stockhist_type;
    private int stockhist_amount;
    private int stockhist_price;
    private Date stockhist_Date;
}
