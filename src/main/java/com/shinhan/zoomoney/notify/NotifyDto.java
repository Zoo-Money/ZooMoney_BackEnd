package com.shinhan.zoomoney.notify;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NotifyDto {
    private int notify_num;
    private int child_num;
    private int parent_num;
    private String notify_content;
    private String notify_url;
    private Timestamp notify_time;
    private boolean notify_check;
}
