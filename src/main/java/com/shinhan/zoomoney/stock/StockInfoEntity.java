package com.shinhan.zoomoney.stock;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="StockInfo")
public class StockInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int infoNum;

    private String infoTitle;
    @Column(length=500)
    private String infoContent;
}
