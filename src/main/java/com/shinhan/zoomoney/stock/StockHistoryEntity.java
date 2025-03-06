package com.shinhan.zoomoney.stock;

import java.util.Date;

import com.shinhan.zoomoney.child.ChildEntity;
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
@Table(name="StockHistory")
public class StockHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stockhistNum;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    @ManyToOne
    @JoinColumn(name = "stock_num")
    private StockEntity stock;

    private String stockhistType;
    private int stockhistAmount;
    private int stockhistPrice;
    private Date stockHistDate;
}
