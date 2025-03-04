package com.shinhan.zoomoney.stock;

import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockResultDto {


    private int result_num;
    private int child_num;
    private Date result_date;
    private double result_rate;
    private int result_rank;
}
