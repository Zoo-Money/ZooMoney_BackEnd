package com.shinhan.zoomoney.moneyplan;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CategoryDto {

    private int categoryNum;
    private String categoryName;
}
