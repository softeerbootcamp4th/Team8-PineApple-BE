package softeer.team_pineapple_be.domain.member.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 인증코드 요청
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginAuthCodeRequest {
  private String phoneNumber;
  private Integer code;
}
