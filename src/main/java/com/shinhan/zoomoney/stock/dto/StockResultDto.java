package com.shinhan.zoomoney.stock.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockResultDto {

    private int result_num;
    private int member_num; // child_num에서 member_num으로 수정
    private Date result_date; // util 타입으로 수정
    private double result_rate;
    private int result_rank;
}
