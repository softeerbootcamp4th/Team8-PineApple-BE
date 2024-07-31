package softeer.team_pineapple_be.domain.draw.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.draw.domain.DrawPrize;
import softeer.team_pineapple_be.domain.draw.repository.DrawPrizeRepository;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;
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
    DrawPrize prize = drawPrizeRepository.findById(prizeId).orElseThrow(() -> new RuntimeException("상품 없음"));
    if (!memberPhoneNumber.equals(prize.getOwner())) {
      throw new RuntimeException("상품 소유자 아님");
    }
    messageService.sendPrizeImage(prize.getImage());
  }
}
