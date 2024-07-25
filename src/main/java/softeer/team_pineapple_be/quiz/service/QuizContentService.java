package softeer.team_pineapple_be.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import softeer.team_pineapple_be.quiz.domain.QuizContent;
import softeer.team_pineapple_be.quiz.repository.QuizContentRepository;
import softeer.team_pineapple_be.quiz.response.QuizContentResponse;

import java.time.LocalDate;

//TODO: 예외처리(구조 맞추기 위해 남겨둠)

/**
 * QuizContent의 요청에 대한 처리를 담당하는 클래스
 */
@Service
@RequiredArgsConstructor
public class QuizContentService {

    private final QuizContentRepository quizContentRepository;

    /**
     * 현재 날짜에 대한 이벤트 내용을 전송해주는 메서드
     * @return 현재 날짜의 이벤트 내용
     */
    public QuizContentResponse quizContent() {
        QuizContent quizContent = quizContentRepository.findByQuizDate(LocalDate.now());
        return QuizContentResponse.quizContentResponse(quizContent);
    }
}
