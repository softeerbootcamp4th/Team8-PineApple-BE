package softeer.team_pineapple_be.domain.worldcup.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 월드컵 결과 응답
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorldCupResultResponse {
  private Integer id;
  private Long count;
  private Long percent;

  public static WorldCupResultResponse from(Integer id, Long count, Long totalCount) {
    return new WorldCupResultResponse(id, count, 100 * count / totalCount);
  }
}
