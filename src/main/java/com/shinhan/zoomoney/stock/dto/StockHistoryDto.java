package com.shinhan.zoomoney.stock.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockHistoryDto {

    private int stockhist_num;
    private int member_num;// child_num에서 member_num으로 수정
    private int stock_num;
    private String stock_id;
    private String stockhist_type;
    private int stockhist_amount;
    private int stockhist_price;
    private Date stockhist_date;// util.date로 수정
    private String stock_name;// stock_name 추가
}
