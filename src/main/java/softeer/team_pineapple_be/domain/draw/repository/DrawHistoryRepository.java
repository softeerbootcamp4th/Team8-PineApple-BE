package softeer.team_pineapple_be.domain.draw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softeer.team_pineapple_be.domain.draw.domain.DrawHistory;

/**
 * 경품 추첨 이력 리포지토리
 */
public interface DrawHistoryRepository extends JpaRepository<DrawHistory, Long> {
}
