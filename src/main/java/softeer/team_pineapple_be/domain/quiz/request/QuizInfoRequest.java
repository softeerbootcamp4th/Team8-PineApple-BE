package softeer.team_pineapple_be.domain.quiz.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 퀴즈 정답 제출을 위한 클래스
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizInfoRequest {

    @NotNull(message = "퀴즈 아이디는 필수입니다.")
    private Integer quizId;

    @NotNull(message = "퀴즈 정답은 필수입니다.")
    private Byte answerNum;
}
