package softeer.team_pineapple_be.domain.comment.utils;

import lombok.experimental.UtilityClass;

/**
 * 전화번호 암호화 유틸 클래스
 */
@UtilityClass
public class PhoneNumberMasker {
  public String maskPhoneNumber(String phoneNumber) {
    StringBuilder stringBuilder = new StringBuilder(phoneNumber);
    stringBuilder.setCharAt(5, 'x');
    stringBuilder.setCharAt(6, 'x');
    stringBuilder.setCharAt(7, 'x');
    stringBuilder.setCharAt(10, 'x');
    stringBuilder.setCharAt(11, 'x');
    stringBuilder.setCharAt(12, 'x');
    return stringBuilder.toString();
  }
}
