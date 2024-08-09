package softeer.team_pineapple_be.domain.quiz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 퀴즈 선착순 상품 엔티티
 */
@Entity
@NoArgsConstructor
@Getter
public class QuizReward {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Integer successOrder;

  @Column(nullable = false)
  private String rewardImage;

  @Column(nullable = false)
  private Boolean valid;

  public QuizReward(Integer successOrder, String rewardImage) {
    this.successOrder = successOrder;
    this.rewardImage = rewardImage;
    this.valid = true;
  }

  public void invalidate() {
    valid = false;
  }
}
