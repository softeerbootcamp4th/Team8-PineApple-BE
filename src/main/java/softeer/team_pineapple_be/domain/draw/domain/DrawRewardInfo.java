package softeer.team_pineapple_be.domain.draw.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 경품 정보 엔티티 : 경품 이름, 경품 재고,경품 이미지
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DrawRewardInfo {
  @Id
  @Column(nullable = false)
  private Byte ranking;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private Integer stock;
  @OneToMany(mappedBy = "drawRewardInfo", fetch = FetchType.LAZY)
  private List<DrawPrize> drawPrizes;

  public void decreaseStock() {
    stock--;
  }
}
