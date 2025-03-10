package com.shinhan.zoomoney.contract;

import java.time.LocalDate;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ContractDto {

    private int contract_num;
    private int child_num;
    private int contract_money;
    private LocalDate contract_date;
    private boolean contract_status;
    private LocalDate contract_provide;
    private String contract_content;
    private String contract_filepath;
    private String contract_imgpath;
    private String contract_excelpath;
}
