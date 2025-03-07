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
    private int notifyNum;
    private int memberNum;
    private String notifyContent;
    private String notifyUrl;
    private Timestamp notifyTime;
    private boolean notifyCheck;
}