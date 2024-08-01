package softeer.team_pineapple_be.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softeer.team_pineapple_be.domain.quiz.domain.QuizContent;

import java.time.LocalDate;
import java.util.Optional;

//TODO: QueryDSL로 리팩토링

/**
 * QuizContent 저장을 위한 인터페이스
 */
@Repository
public interface QuizContentRepository extends JpaRepository<QuizContent, Integer> {

    /**
     * 날짜를 기준으로 DB에서 조회하는 메서드
     * @param quizDate 조회하고자 하는 날짜
     * @return 조회된 QuizContent
     */
    Optional<QuizContent> findByQuizDate(LocalDate quizDate);
}
