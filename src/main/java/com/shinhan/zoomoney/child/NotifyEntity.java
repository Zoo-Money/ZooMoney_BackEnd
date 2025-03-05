package com.shinhan.zoomoney.child;

import com.shinhan.zoomoney.parent.ParentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="Notify")
public class NotifyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notify_num;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    @ManyToOne
    @JoinColumn(name = "parent_num")
    private ParentEntity parent;

    private String notify_content;
    private String notify_url;
    private Timestamp notify_time;
    private boolean notify_check;
}
