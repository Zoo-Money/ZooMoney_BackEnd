package com.shinhan.zoomoney.stock.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "StockInfo")
public class StockInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int infoNum;

    private String infoTitle;
    @Column(length = 500)
    private String infoContent;
}
