package com.shinhan.zoomoney.child;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChildDto {

    private int child_num;
    private String child_id;
    private String child_pw;
    private String child_name;
    private String child_phone;
    private int child_point;
}
