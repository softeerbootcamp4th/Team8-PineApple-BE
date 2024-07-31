package softeer.team_pineapple_be.domain.draw.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일자별 메시지 정보 엔티티
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DrawDailyMessageInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer Id;
  @Column(nullable = false)
  private String winMessage;
  @Column(nullable = false)
  private String loseMessage;
  @Column(nullable = false)
  private String loseScenario;
  @Column(nullable = false)
  private String winImage;
  @Column(nullable = false)
  private String loseImage;
  @Column(nullable = false)
  private LocalDate drawDate;
}
