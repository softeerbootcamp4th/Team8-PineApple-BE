package softeer.team_pineapple_be.domain.draw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softeer.team_pineapple_be.domain.draw.domain.DrawRewardInfo;

/**
 * 경품 정보 리포지토리
 */
public interface DrawRewardInfoRepository extends JpaRepository<DrawRewardInfo, Byte> {
}
