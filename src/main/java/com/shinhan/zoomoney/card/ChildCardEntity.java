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
    private String cardNum;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    private String cardMetadata;
    private int cardMoney;
}
