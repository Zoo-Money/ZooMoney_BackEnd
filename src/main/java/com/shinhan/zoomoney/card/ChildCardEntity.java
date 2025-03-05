package com.shinhan.zoomoney.card;

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
@Table(name="ChildCard")
public class ChildCardEntity {
    @Id
    private String card_num;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity Child;

    private String card_metadata;
    private int card_money;
}
