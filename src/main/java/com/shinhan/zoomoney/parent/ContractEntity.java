package com.shinhan.zoomoney.parent;

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
@Table(name="Contract")
public class ContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contract_num;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    private int contract_money;
    private Date contract_date;
    private boolean contract_status;
    private Date contract_provide;
    private String contract_content;
    private String contract_filepath;
    private String contract_imgpath;
    private String contract_excelpath;
}
