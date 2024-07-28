package softeer.team_pineapple_be.domain.quiz.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import softeer.team_pineapple_be.domain.quiz.domain.QuizInfo;

/**
 * 퀴즈 정답 판단 내용을 안내하기 위한 클래스
 */
@Getter
@AllArgsConstructor
public class QuizInfoResponse {

    private Boolean isCorrect;
    private String quizImage;
    private String quizDescription;
    private String answerContent;

    public static QuizInfoResponse of(QuizInfo quizInfo, Boolean isCorrect){
        return new QuizInfoResponse(
                isCorrect,
                quizInfo.getQuizImage(),
                quizInfo.getQuizDescription(),
                quizInfo.getAnswerContent());
    }
}
