package com.shinhan.zoomoney.stock.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;


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
    private String stock_name;
    private String stockhist_type;
    private int stockhist_amount;
    private int stockhist_price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private Date stockhist_date;
}
