package softeer.team_pineapple_be.global.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.global.auth.context.AuthContext;
import softeer.team_pineapple_be.global.auth.context.AuthContextHolder;
import softeer.team_pineapple_be.global.auth.exception.AuthErrorCode;
import softeer.team_pineapple_be.global.exception.RestApiException;

/**
 * 인증된 사용자 정보 가져오기
 */
@Service
@RequiredArgsConstructor
public class AuthMemberService {
  /**
   * 인증 컨텍스트 반환
   *
   * @return 인증 컨텍스트
   */
  public AuthContext getAuthContext() {
    return AuthContextHolder.getAuthContext();
  }

  /**
   * 인증된 멤버의 핸드폰번호를 세션에서 반환
   *
   * @return 핸드폰 번호 String
   */
  public String getMemberPhoneNumber() {
    AuthContext authContext = getAuthContext();
    if (authContext == null) {
      throw new RestApiException(AuthErrorCode.NO_USER_INFO);
    }
    return authContext.getPhoneNumber();
  }

}

