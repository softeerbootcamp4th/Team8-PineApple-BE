package softeer.team_pineapple_be.domain.draw.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 경품 발송 요청 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendPrizeRequest {
  @NotNull(message = "{draw.prize_id_required}")
  private Long prizeId;
}
