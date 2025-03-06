package com.shinhan.zoomoney.quiz;

import java.util.Date;

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
@Table(name="Quiz")
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int quizNum;

    @ManyToOne
    @JoinColumn(name = "child_num")
    private ChildEntity child;

    private boolean quizCheck;
    private Date quizDate;
}
