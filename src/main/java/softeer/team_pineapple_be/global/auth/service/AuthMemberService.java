package softeer.team_pineapple_be.global.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * 인증된 사용자 정보 가져오기
 */
@Service
@RequiredArgsConstructor
public class AuthMemberService {
  /**
   * 인증된 멤버의 핸드폰번호를 세션에서 반환
   *
   * @return 핸드폰 번호 String
   */
  public String getMemberPhoneNumber() {
    Object phoneNumber = getCurrentRequest().getSession().getAttribute("phoneNumber");
    //TODO : 세션정보 존재하지 않으면 예외처리
    return (String) phoneNumber;
  }

  private HttpServletRequest getCurrentRequest() {
    return getRequestAttributes().getRequest();
  }

  private ServletRequestAttributes getRequestAttributes() {
    return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
  }
}
