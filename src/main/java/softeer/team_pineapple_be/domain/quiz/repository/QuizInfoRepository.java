package softeer.team_pineapple_be.domain.quiz.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softeer.team_pineapple_be.domain.quiz.domain.QuizInfo;

//TODO: QueryDSL로 리팩토링

/**
 * QuizInformation 저장을 위한 인터페이스
 */
@Repository
public interface QuizInfoRepository extends JpaRepository<QuizInfo, Integer> {

}
