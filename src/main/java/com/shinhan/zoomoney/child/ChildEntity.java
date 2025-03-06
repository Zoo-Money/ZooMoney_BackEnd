package com.shinhan.zoomoney.child;

import com.shinhan.zoomoney.parent.ParentEntity;
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
@Table(name="Child")
public class ChildEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int childNum;

    @ManyToOne
    @JoinColumn(name = "parent_num")
    private ParentEntity parent;

    private String childId;
    private String childPw;
    private String childName;
    private String childPhone;
    private int childPoint;




}
