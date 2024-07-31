package softeer.team_pineapple_be.domain.draw.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 경품 추첨 꽝 응답
 */
@Getter
@Setter
@NoArgsConstructor
public class DrawLoseResponse extends DrawResponse {
  private String dailyLoseMessage;
  private String dailyLoseScenario;
  private String image;
  private Boolean car;
  private Integer toolBoxCount;

  public DrawLoseResponse(String dailyLoseMessage, String dailyLoseScenario, String image, Boolean car,
      Integer toolBoxCount) {
    isDrawWin = false;
    this.dailyLoseMessage = dailyLoseMessage;
    this.dailyLoseScenario = dailyLoseScenario;
    this.image = image;
    this.car = car;
    this.toolBoxCount = toolBoxCount;
  }
}
