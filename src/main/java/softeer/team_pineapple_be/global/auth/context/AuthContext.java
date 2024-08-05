package softeer.team_pineapple_be.global.auth.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 인증 컨텍스트 객체
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthContext {
  private String phoneNumber;
  private String role;
}
