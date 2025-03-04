package com.shinhan.zoomoney.parent;

import lombok.*;

import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ContractDto {

    private int contract_num;
    private int child_num;
    private int contract_money;
    private Date contract_date;
    private boolean contract_status;
    private Date contract_provide;
    private String contract_content;
    private String contract_filepath;
    private String contract_imgpath;
    private String contract_excelpath;
}
