package softeer.team_pineapple_be.domain.draw.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.draw.request.SendPrizeRequest;
import softeer.team_pineapple_be.domain.draw.response.DrawResponse;
import softeer.team_pineapple_be.domain.draw.service.DrawPrizeService;
import softeer.team_pineapple_be.domain.draw.service.DrawService;
import softeer.team_pineapple_be.global.common.response.SuccessResponse;

/**
 * 경품 추첨 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/draw")
public class DrawController {
  private final DrawService drawService;
  private final DrawPrizeService drawPrizeService;

  @PostMapping
  public ResponseEntity<DrawResponse> enterDraw() {
    return ResponseEntity.ok(drawService.enterDraw());
  }

  @PostMapping("/rewards/send-prize")
  public ResponseEntity<SuccessResponse> sendPrize(@RequestBody SendPrizeRequest request) {
    drawPrizeService.sendPrizeMessage(request.getPrizeId());
    return ResponseEntity.ok(new SuccessResponse());
  }
}
