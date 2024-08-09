package softeer.team_pineapple_be.domain.fcfs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import softeer.team_pineapple_be.domain.fcfs.domain.FcfsInfoEntity;

/**
 * 선착순 정보 리포지토리
 */
public interface FcfsInfoRepository extends JpaRepository<FcfsInfoEntity, Long> {
  Optional<FcfsInfoEntity> findByParticipantId(String participantId);
}
