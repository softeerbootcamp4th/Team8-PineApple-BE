package softeer.team_pineapple_be.domain.member.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 전화번호 로그인 요청
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class LoginPhoneNumberRequest {
  @NotNull(message = "핸드폰 번호는 필수입니다.")
  private String phoneNumber;
}
