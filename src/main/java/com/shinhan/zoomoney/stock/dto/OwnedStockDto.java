package com.shinhan.zoomoney.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnedStockDto {
    private String stockName;
    private String stockId;
    private int quantity;
    private double averagePrice;
    private double totalValue;
    private int stockhistPrice;
    private int stockPrice;

}
