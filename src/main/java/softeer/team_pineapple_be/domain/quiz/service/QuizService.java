package softeer.team_pineapple_be.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import softeer.team_pineapple_be.domain.quiz.domain.QuizContent;
import softeer.team_pineapple_be.domain.quiz.domain.QuizInfo;
import softeer.team_pineapple_be.domain.quiz.repository.QuizContentRepository;
import softeer.team_pineapple_be.domain.quiz.repository.QuizInfoRepository;
import softeer.team_pineapple_be.domain.quiz.request.QuizInfoRequest;
import softeer.team_pineapple_be.domain.quiz.response.QuizContentResponse;
import softeer.team_pineapple_be.domain.quiz.response.QuizInfoResponse;

import java.time.LocalDate;
import java.util.Optional;

//TODO: 예외처리(구조 맞추기 위해 남겨둠)

/**
 * QuizContent의 요청에 대한 처리를 담당하는 클래스
 */
@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizContentRepository quizContentRepository;
    private final QuizInfoRepository quizInfoRepository;

    //TODO: 선착순 처리 되어야함(선착순 처리에 대한 정보 전송할 시에 Response까지 수정되어야 함.)

    /**
     * 퀴즈 정답에 대한 여부를 판단하고 내용을 전송해주는 메서드
     * @param quizInfoRequest 퀴즈 번호와 사용자가 제출한 정답을 받아오기 위한 객체
     * @return 정답 안내 정보에 대한 내용
     */
    public QuizInfoResponse quizIsCorrect(QuizInfoRequest quizInfoRequest) {
        Optional<QuizInfo> optionalQuizInfo = quizInfoRepository.findById(quizInfoRequest.getQuizId());
        if (optionalQuizInfo.isEmpty()) {
            // TODO: 퀴즈 존재 안 하면 예외처리
        }
        QuizInfo quizInfo = optionalQuizInfo.get();
        if(quizInfoRequest.getAnswerNum().equals(quizInfo.getAnswerNum())){
            return QuizInfoResponse.of(quizInfo, true);
        }
        return QuizInfoResponse.of(quizInfo, false);
    }

    /**
     * 현재 날짜에 대한 이벤트 내용을 전송해주는 메서드
     * @return 현재 날짜의 이벤트 내용
     */
    public QuizContentResponse quizContent() {
        QuizContent quizContent = quizContentRepository.findByQuizDate(LocalDate.now());
        return QuizContentResponse.of(quizContent);
    }
}
