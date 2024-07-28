package softeer.team_pineapple_be.domain.quiz.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import softeer.team_pineapple_be.domain.quiz.domain.QuizContent;

/**
 * QuizContent의 응답을 구성하는 클래스
 */
@AllArgsConstructor
@Getter
public class QuizContentResponse {

    private Integer id;
    private String quizDescription;
    private String quizQuesion1;
    private String quizQuesion2;
    private String quizQuesion3;
    private String quizQuesion4;

    /**
     * QuizContent의 엔티티를 응답 형식으로 변환하는 메서드
     * @param quizContent 변환하고자 하는 QuizContent
     * @return 응답 형식에 맞는 QuizContent
     */
    public static QuizContentResponse of(QuizContent quizContent){
        return new QuizContentResponse(
                quizContent.getId(),
                quizContent.getQuizDescription(),
                quizContent.getQuizQuestion1(),
                quizContent.getQuizQuestion2(),
                quizContent.getQuizQuestion3(),
                quizContent.getQuizQuestion4());
    }
}
