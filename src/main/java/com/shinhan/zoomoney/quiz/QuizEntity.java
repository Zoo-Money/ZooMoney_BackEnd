package com.shinhan.zoomoney.quiz;

import com.shinhan.zoomoney.child.ChildEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="Quiz")
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int quiz_num;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    private boolean quiz_check;
    private Date quiz_date;
}
