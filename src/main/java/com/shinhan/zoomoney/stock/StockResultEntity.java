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
@Table(name="StockResult")
public class StockResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resultNum;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    private Date resultDate;
    private double resultRate;
    private int resultRank;
}
