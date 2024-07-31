package softeer.team_pineapple_be.domain.draw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import softeer.team_pineapple_be.domain.draw.domain.DrawPrize;
import softeer.team_pineapple_be.domain.draw.domain.DrawRewardInfo;

/**
 * 경품 리포지토리
 */
public interface DrawPrizeRepository extends JpaRepository<DrawPrize, Long> {
  Optional<DrawPrize> findFirstByDrawRewardInfoAndValid(DrawRewardInfo drawRewardInfo, boolean valid);
}
