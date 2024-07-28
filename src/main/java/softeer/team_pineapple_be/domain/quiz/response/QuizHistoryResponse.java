package softeer.team_pineapple_be.domain.quiz.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import softeer.team_pineapple_be.domain.member.domain.Member;

//TODO: 열쇠를 반환해줘야 하나..? 세션에 열쇠 정보를 담고 있나?!
@AllArgsConstructor
@Getter
public class QuizHistoryResponse {

    private Integer toolBoxCnt;

    /**
     * QuizHistory의 엔티티를 응답 형식으로 변환하는 메서드
     * @param member 기본키가 되는 멤버의 id를 위한 매개변수
     * @return 반환되는 응답 형식
     */
    public static QuizHistoryResponse of(Member member) {
        return new QuizHistoryResponse(member.getToolBoxCnt());
    }
}
