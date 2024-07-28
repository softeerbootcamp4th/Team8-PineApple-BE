package softeer.team_pineapple_be.domain.quiz.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 퀴즈 정답 제출을 위한 클래스
 */
@Getter
@AllArgsConstructor
public class QuizInfoRequest {

    private Integer quizId;
    private Byte answerNum;
}
