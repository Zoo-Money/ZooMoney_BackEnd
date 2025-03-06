package com.shinhan.zoomoney.daily;

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
@Table(name="Daily")
public class DailyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dailyNum;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    private boolean dailyCheck;
    private Date dailyDate;
}
