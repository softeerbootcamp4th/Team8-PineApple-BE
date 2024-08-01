package softeer.team_pineapple_be.domain.member.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 인증코드 로그인 요청
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginAuthCodeRequest {
  @NotNull(message = "핸드폰 번호는 필수입니다.")
  private String phoneNumber;
  @NotNull(message = "코드는 필수입니다.")
  private Integer code;
}
