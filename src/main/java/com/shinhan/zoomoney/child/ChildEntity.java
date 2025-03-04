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
    private int child_num;

    @ManyToOne
    @JoinColumn(name = "parent_num")
    private ParentEntity parent_num;

    private String child_id;
    private String child_pw;
    private String child_name;
    private String child_phone;
    private int child_point;




}
