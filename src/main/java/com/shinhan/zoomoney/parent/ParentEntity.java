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
    private int parent_num;

    private String parent_id;
    private String parent_pw;
    private String parent_name;
    private String parent_phone;
    private String parent_account;
}
