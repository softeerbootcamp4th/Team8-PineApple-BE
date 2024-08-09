package softeer.team_pineapple_be.domain.fcfs.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 선착순 정보 저장
 */
@Entity
@Getter
@NoArgsConstructor
public class FcfsInfoEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String participantId;

  @Column(nullable = false)
  private Integer successOrder;

  public FcfsInfoEntity(String participantId, Integer successOrder) {
    this.participantId = participantId;
    this.successOrder = successOrder;
  }
}
