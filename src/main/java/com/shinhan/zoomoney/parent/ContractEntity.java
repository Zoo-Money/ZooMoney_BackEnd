package com.shinhan.zoomoney.parent;



import java.time.LocalDate;

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
@Table(name="Contract")
public class ContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contractNum;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    private int contractMoney;
    private LocalDate contractDate;
    private boolean contractStatus;
    private LocalDate contractProvide;
    private String contractContent;
    private String contractFilepath;
    private String contractImgpath;
    private String contractExcelpath;
}
