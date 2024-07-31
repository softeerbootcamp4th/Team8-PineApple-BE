package softeer.team_pineapple_be.domain.draw.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 경품 추첨 당첨 응답
 */
@Getter
@Setter
@NoArgsConstructor
public class DrawWinningResponse extends DrawResponse {
  private String dailyWinningMessage;
  private String prizeName;
  private String image;
  private Long prizeId;
  private Boolean car;
  private Integer toolBoxCount;

  public DrawWinningResponse(String dailyWinningMessage, String prizeName, String image, Long prizeId, Boolean car,
      Integer toolBoxCount) {
    isDrawWin = true;
    this.dailyWinningMessage = dailyWinningMessage;
    this.prizeName = prizeName;
    this.image = image;
    this.prizeId = prizeId;
    this.car = car;
    this.toolBoxCount = toolBoxCount;
  }
}
