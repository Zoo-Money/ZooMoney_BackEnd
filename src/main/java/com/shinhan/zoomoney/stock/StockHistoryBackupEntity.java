package com.shinhan.zoomoney.stock;

import com.shinhan.zoomoney.child.ChildEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="StockHistoryBackup")
public class StockHistoryBackupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stockhist_num;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child_num;

    @ManyToOne
    @JoinColumn(name = "stock_num")
    private StockEntity stock_num;

    private String stockhist_type;
    private int stockhist_amount;
    private int stockhist_price;
    private Date stockhist_date;
}
