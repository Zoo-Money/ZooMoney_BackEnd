package com.shinhan.zoomoney.stock;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockInfoDto {
    private int info_num;
    private String info_content;
}
