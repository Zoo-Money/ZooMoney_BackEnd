package com.shinhan.zoomoney.stock;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class StockDto {

    private int stock_num;
    private String stock_name;
    private String stock_id;
    private String stock_info;
}
