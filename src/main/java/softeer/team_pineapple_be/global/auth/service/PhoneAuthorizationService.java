package softeer.team_pineapple_be.global.auth.service;

import org.springframework.stereotype.Service;

/**
 * 핸드폰 인증번호 서비스
 */
@Service
public class PhoneAuthorizationService {
  /**
   * 사용자에게 인증코드를 문자로 보내고, 해당 인증코드를 반환한다.
   *
   * @param phoneNumber
   * @return 인증코드
   */
  public Integer sendAuthMessage(String phoneNumber) {
    //TODO : 이후 문자기능 연동하면, 문자로 authCode 를 보내주고, AuthCode를 반환해야함
    //Integer authCode = RandomUtils.getAuthCode();

    return 111111;
  }
}
