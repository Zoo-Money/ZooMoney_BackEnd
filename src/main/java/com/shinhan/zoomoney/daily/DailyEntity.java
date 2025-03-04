package com.shinhan.zoomoney.daily;

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
    private int daily_num;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child_num;

    private boolean daily_check;
    private java.sql.Date daily_date;
}
