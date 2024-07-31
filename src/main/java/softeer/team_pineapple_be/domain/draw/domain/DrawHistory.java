package softeer.team_pineapple_be.domain.draw.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import softeer.team_pineapple_be.global.common.domain.BaseTimeEntity;

/**
 * 경품 추첨 이력 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class DrawHistory extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;
  @Column(nullable = false)
  private Byte drawResult;
  @Column(nullable = false)
  private String phoneNumber;

  public DrawHistory(Byte drawResult, String phoneNumber) {
    this.drawResult = drawResult;
    this.phoneNumber = phoneNumber;
  }
}
