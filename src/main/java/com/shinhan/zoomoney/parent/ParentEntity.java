package com.shinhan.zoomoney.parent;

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
@Table(name="Parent")
public class ParentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int parentNum;

    private String parentId;
    private String parentPw;
    private String parentName;
    private String parentPhone;
    private String parentAccount;
}
