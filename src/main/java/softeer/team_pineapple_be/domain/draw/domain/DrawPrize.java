package softeer.team_pineapple_be.domain.draw.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 경품 QR코드나 바코드 사진 저장 엔티티
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DrawPrize {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String image;
  private Boolean valid;
  private String owner;
  @ManyToOne
  private DrawRewardInfo drawRewardInfo;

  public void invalidate() {
    this.valid = false;
  }

  public void isNowOwnedBy(String phoneNumber) {
    this.owner = phoneNumber;
  }
}
