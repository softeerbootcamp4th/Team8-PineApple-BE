package softeer.team_pineapple_be.domain.draw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

import softeer.team_pineapple_be.domain.draw.domain.DrawDailyMessageInfo;

/**
 * 일자별 경품 추첨 메시지 리포지토리
 */
public interface DrawDailyMessageInfoRepository extends JpaRepository<DrawDailyMessageInfo, Integer> {
  Optional<DrawDailyMessageInfo> findByDrawDate(LocalDate drawDate);
}
