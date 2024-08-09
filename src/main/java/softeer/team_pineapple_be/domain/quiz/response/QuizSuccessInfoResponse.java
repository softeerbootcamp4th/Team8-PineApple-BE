package softeer.team_pineapple_be.domain.quiz.response;

import lombok.Getter;

/**
 * 퀴즈 정답 맞췄을 때의 응답
 */
@Getter
public class QuizSuccessInfoResponse extends QuizInfoResponse {
  private String quizParticipantId;
  private Integer successOrder;

  public QuizSuccessInfoResponse(Boolean isCorrect, String quizImage, String quizParticipantId, Integer successOrder) {
    super(isCorrect, quizImage);
    this.quizParticipantId = quizParticipantId;
    this.successOrder = successOrder;
  }
}
