package com.shinhan.zoomoney.stock;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="StockMoney")
public class StockMoneyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int child_num;

    private int stockmoney_total;
}
