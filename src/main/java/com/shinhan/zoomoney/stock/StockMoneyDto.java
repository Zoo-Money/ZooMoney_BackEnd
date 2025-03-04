package com.shinhan.zoomoney.stock;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockMoneyDto {
    private int child_num;
    private int stockmoney_total;
}
