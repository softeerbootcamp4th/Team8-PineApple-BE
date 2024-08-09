package softeer.team_pineapple_be.domain.quiz.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import softeer.team_pineapple_be.domain.quiz.domain.QuizReward;

/**
 * 퀴즈 경품 리포지토리
 */
public interface QuizRewardRepository extends CrudRepository<QuizReward, Long> {
  Optional<QuizReward> findBySuccessOrder(Integer successOrder);
}
