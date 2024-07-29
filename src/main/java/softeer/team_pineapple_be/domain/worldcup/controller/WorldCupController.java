package softeer.team_pineapple_be.domain.worldcup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.worldcup.response.WorldCupParticipateResponse;
import softeer.team_pineapple_be.domain.worldcup.service.WorldCupService;
import softeer.team_pineapple_be.global.common.response.SuccessResponse;

/**
 * 월드컵 컨트롤러
 */
@Tag(name = "월드컵 API", description = "월드컵 참여 기능")
@RestController
@RequestMapping("/worldcup")
@RequiredArgsConstructor
public class WorldCupController {
  private final WorldCupService worldCupService;

  @Operation(summary = "월드컵 참여했는지 확인")
  @GetMapping("/participate")
  public ResponseEntity<WorldCupParticipateResponse> getParticipate() {
    WorldCupParticipateResponse worldCupParticipateResponse = worldCupService.isMemberParticipated();
    return ResponseEntity.ok(worldCupParticipateResponse);
  }

  @Operation(summary = "월드컵 참여 여부 등록")
  @PostMapping("/participate")
  public ResponseEntity<SuccessResponse> participate() {
    worldCupService.participateWorldCup();
    return ResponseEntity.ok(new SuccessResponse());
  }
}