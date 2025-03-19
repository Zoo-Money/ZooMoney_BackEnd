package com.shinhan.zoomoney.stock.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockInfoDto {
    private int infoNum;
    private String infoTitle;
    private String infoContent;
}
