package softeer.team_pineapple_be.domain.draw.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 당첨 확률 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DrawProbability {
  @Id
  @Column(nullable = false)
  private Byte ranking;
  @Column(nullable = false)
  private Integer probability;
}
