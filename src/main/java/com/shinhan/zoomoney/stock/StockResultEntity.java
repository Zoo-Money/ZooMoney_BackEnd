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
@Table(name="StockResult")
public class StockResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int result_num;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    private Date result_date;
    private double result_rate;
    private int result_rank;
}
