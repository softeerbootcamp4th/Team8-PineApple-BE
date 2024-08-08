package softeer.team_pineapple_be.domain.quiz.response;


import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import softeer.team_pineapple_be.domain.quiz.domain.QuizContent;

/**
 * QuizContent의 응답을 구성하는 클래스
 */
@AllArgsConstructor
@Getter
public class QuizContentResponse {

  private Integer quizId;
  private String quizDescription;
  private Map<Integer, String> quizQuestions;

  /**
   * QuizContent의 엔티티를 응답 형식으로 변환하는 메서드
   *
   * @param quizContent 변환하고자 하는 QuizContent
   * @return 응답 형식에 맞는 QuizContent
   */
  public static QuizContentResponse of(QuizContent quizContent) {
    Map<Integer, String> questions = new HashMap<>();
    questions.put(1, quizContent.getQuizQuestion1());
    questions.put(2, quizContent.getQuizQuestion2());
    questions.put(3, quizContent.getQuizQuestion3());
    questions.put(4, quizContent.getQuizQuestion4());

    return new QuizContentResponse(quizContent.getId(), quizContent.getQuizDescription(), questions);
  }
}
