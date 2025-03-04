package com.shinhan.zoomoney.parent;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ParentDto {


    private int parent_num;
    private String parent_id;
    private String parent_pw;
    private String parent_name;
    private String parent_phone;
    private String parent_account;

}
