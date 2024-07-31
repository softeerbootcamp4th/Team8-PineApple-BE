package softeer.team_pineapple_be.global.message;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 메시지 서비스 인터페이스
 */
@Service
@Slf4j
public class MessageService {
  public boolean sendPrizeImage(String message) {
    //TODO : 이후 SDK 사용해서 경품 이미지 보내는 로직 구현해야함
    //TODO : S3에서 받아와서 보내기
    log.info("경품 이미지 발송!");
    return true;
  }
}
