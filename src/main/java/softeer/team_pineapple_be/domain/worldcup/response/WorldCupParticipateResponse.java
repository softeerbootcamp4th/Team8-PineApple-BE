package softeer.team_pineapple_be.domain.worldcup.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 월드컵 참여여부 응답
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorldCupParticipateResponse {
  private Boolean car;
}
