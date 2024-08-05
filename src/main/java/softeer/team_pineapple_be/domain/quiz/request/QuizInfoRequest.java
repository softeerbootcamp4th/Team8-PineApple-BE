package softeer.team_pineapple_be.domain.quiz.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

/**
 * 퀴즈 정답 제출을 위한 클래스
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizInfoRequest {

    @NotNull(message = "{quiz.id_required}")
    private Integer quizId;

    @NotNull(message = "{quiz.answer_required}")
    @Range(min = 1, max = 4, message = "{quiz.answer_num_range}")
    private Byte answerNum;
}
