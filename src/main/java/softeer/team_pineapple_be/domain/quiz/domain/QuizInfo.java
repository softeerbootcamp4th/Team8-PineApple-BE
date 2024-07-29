package softeer.team_pineapple_be.domain.quiz.domain;


import jakarta.persistence.*;
import lombok.Getter;

//TODO: quizDescription 넣어야 될지 얘기해보기
/**
 * QuizInfo의 엔티티 타입
 * Quiz의 정답 정보 및 이미지 저장
 */
@Entity
@Getter
public class QuizInfo {

    @Id
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id")
    private QuizContent quizContent;

    @Column(nullable = false)
    private Byte answerNum;

    @Column(nullable = false)
    private String quizImage;

}
