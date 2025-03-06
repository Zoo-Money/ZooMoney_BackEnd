package com.shinhan.zoomoney.notify;

import java.sql.Timestamp;

import com.shinhan.zoomoney.child.ChildEntity;
import com.shinhan.zoomoney.parent.ParentEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Notify")
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
