package softeer.team_pineapple_be.domain.draw.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.draw.domain.DrawPrize;
import softeer.team_pineapple_be.domain.draw.exception.DrawErrorCode;
import softeer.team_pineapple_be.domain.draw.repository.DrawPrizeRepository;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;
import softeer.team_pineapple_be.global.exception.RestApiException;
import softeer.team_pineapple_be.global.message.MessageService;

/**
 * 경품 서비스
 */
@Service
@RequiredArgsConstructor
public class DrawPrizeService {
  private final DrawPrizeRepository drawPrizeRepository;
  private final MessageService messageService;
  private final AuthMemberService authMemberService;

  @Transactional
  public void sendPrizeMessage(Long prizeId) {
    String memberPhoneNumber = authMemberService.getMemberPhoneNumber();
    DrawPrize prize = drawPrizeRepository.findById(prizeId).orElseThrow(() -> new RestApiException(DrawErrorCode.NO_PRIZE));
    if (!memberPhoneNumber.equals(prize.getOwner())) {
      throw new RestApiException(DrawErrorCode.NOT_PRIZE_OWNER);
    }
    messageService.sendPrizeImage(prize.getImage());
  }
}
