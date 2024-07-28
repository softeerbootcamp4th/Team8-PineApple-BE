package softeer.team_pineapple_be.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softeer.team_pineapple_be.domain.quiz.domain.QuizHistory;

import java.time.LocalDate;
import java.util.List;

//TODO: QueryDSL로 리팩토링
/**
 * QuizHistory 저장을 위한 인터페이스
 */
public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {
    List<QuizHistory> findByMemberPhoneNumber(String phoneNumber);
    QuizHistory findByMemberPhoneNumberAndParticipantDate(String phoneNumber, LocalDate participantDate);
}
