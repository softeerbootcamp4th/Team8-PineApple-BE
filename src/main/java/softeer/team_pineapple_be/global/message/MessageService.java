package softeer.team_pineapple_be.global.message;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;

/**
 * 메시지 서비스 인터페이스
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
  private final AuthMemberService authMemberService;

  public boolean sendPrizeImage(String message) {
    //TODO : 이후 SDK 사용해서 경품 이미지 보내는 로직 구현해야함
    //TODO : S3에서 받아와서 보내기
    String memberPhoneNumber = authMemberService.getMemberPhoneNumber();
    log.info(memberPhoneNumber + "에게 경품 이미지 [" + message + "] 발송!");
    return true;
  }
}
