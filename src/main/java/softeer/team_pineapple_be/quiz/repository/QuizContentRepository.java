package softeer.team_pineapple_be.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softeer.team_pineapple_be.quiz.domain.QuizContent;

import java.time.LocalDate;

//TODO: QueryDSL로 리팩토링

/**
 * QuizContent 저장을 위한 인터페이스
 */
@Repository
public interface QuizContentRepository extends JpaRepository<QuizContent, Integer> {

    QuizContent findByQuizDate(LocalDate quizDate);
}
