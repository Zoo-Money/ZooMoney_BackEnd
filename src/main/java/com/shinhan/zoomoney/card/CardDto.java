package com.shinhan.zoomoney.card;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CardDto {

    private String card_num;
    private int child_num;
    private String card_metadata;
    private int card_money;
}
