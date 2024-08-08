package softeer.team_pineapple_be.domain.worldcup.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 월드컵 참여 정보 등록 요청
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorldCupResultRequest {
  @NotNull(message = "{worldcup.id_null}")
  Integer id;
}
