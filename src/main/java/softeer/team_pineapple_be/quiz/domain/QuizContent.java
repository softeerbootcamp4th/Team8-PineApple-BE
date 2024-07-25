package softeer.team_pineapple_be.quiz.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class QuizContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String quizDescription;

    @Column(nullable = false, name = "QUIZ_QUESTION_1")
    private String quizQuestion1;

    @Column(nullable = false, name = "QUIZ_QUESTION_2")
    private String quizQuestion2;

    @Column(nullable = false, name = "QUIZ_QUESTION_3")
    private String quizQuestion3;

    @Column(nullable = false, name = "QUIZ_QUESTION_4")
    private String quizQuestion4;

    @Column(nullable = false)
    private LocalDate quizDate;
}
