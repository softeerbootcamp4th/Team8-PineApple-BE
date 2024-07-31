package softeer.team_pineapple_be.domain.draw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softeer.team_pineapple_be.domain.draw.domain.DrawProbability;

/**
 * 당첨 확률 리포지토리
 */
public interface DrawProbabilityRepository extends JpaRepository<DrawProbability, Byte> {
}
