package softeer.team_pineapple_be.domain.quiz.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 퀴즈 경품 요청 객체
 */
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class QuizRewardRequest {
  @NotNull(message = "{quiz.participantId_null}")
  private String participantId;
}
