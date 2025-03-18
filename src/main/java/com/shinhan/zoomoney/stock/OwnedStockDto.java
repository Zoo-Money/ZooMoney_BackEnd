package com.shinhan.zoomoney.stock;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnedStockDto {
	private String stockName;
    private int quantity;
    private double averagePrice;
    private double totalValue;
    private int stockhistPrice;
    private int stockPrice;
    
}
