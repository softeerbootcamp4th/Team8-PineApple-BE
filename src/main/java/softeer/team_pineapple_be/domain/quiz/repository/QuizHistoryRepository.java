package softeer.team_pineapple_be.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softeer.team_pineapple_be.domain.quiz.domain.QuizContent;
import softeer.team_pineapple_be.domain.quiz.domain.QuizHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//TODO: QueryDSL로 리팩토링
/**
 * QuizHistory 저장을 위한 인터페이스
 */
public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {
    Optional<QuizHistory> findByMemberPhoneNumberAndQuizContentId(String phoneNumber, Integer quizContentId);
}
