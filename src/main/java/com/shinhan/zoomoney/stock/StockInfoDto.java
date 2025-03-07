package com.shinhan.zoomoney.stock;

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
