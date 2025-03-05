package com.shinhan.zoomoney.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private int info_num;

    private String info_content;

}
