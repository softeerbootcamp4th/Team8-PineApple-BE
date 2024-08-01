package softeer.team_pineapple_be.domain.quiz.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @NotNull(message = "{quiz.id_required}")
    private Integer quizId;

    @NotNull(message = "{quiz.answer_required}")
    @Min(value = 1, message = "{quiz.answer_num_range_message}")
    @Max(value = 4, message = "{quiz.answer_num_range_message}")
    private Byte answerNum;
}
