package com.shinhan.zoomoney.stock;

import java.util.Map;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Setter @Getter

public class StockDto {

    private Integer stock_num;
    private String stock_name;
    private String stock_id;
    private String stock_info;
    
    // StockDto -> StockEntity 변환
    public static StockEntity toEntity(StockDto dto) {
    	return StockEntity.builder()
                .stockName(dto.getStock_name())
                .stockId(dto.getStock_id())
                .stockInfo(dto.getStock_info())
                .build();
    }
    
    // StockEntity -> StockDto 변환
    public static StockDto fromEntity(StockEntity entity, String stockInfo) {
        return StockDto.builder()
                .stock_name(entity.getStockName())
                .stock_id(entity.getStockId())
                .stock_info(stockInfo)
                .build();
    }
    
    
}
