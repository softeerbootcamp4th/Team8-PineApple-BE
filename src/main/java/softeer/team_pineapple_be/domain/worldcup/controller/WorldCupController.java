package softeer.team_pineapple_be.domain.worldcup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.worldcup.request.WorldCupResultRequest;
import softeer.team_pineapple_be.domain.worldcup.response.WorldCupParticipateResponse;
import softeer.team_pineapple_be.domain.worldcup.response.WorldCupResultResponse;
import softeer.team_pineapple_be.domain.worldcup.service.WorldCupService;
import softeer.team_pineapple_be.global.auth.annotation.Auth;
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

  @Auth
  @Operation(summary = "월드컵 참여했는지 확인")
  @GetMapping("/participants")
  public ResponseEntity<WorldCupParticipateResponse> getParticipate() {
    WorldCupParticipateResponse worldCupParticipateResponse = worldCupService.isMemberParticipated();
    return ResponseEntity.ok(worldCupParticipateResponse);
  }

  @Auth
  @Operation(summary = "월드컵 참여 여부 등록")
  @PostMapping("/participants")
  public ResponseEntity<SuccessResponse> participate() {
    worldCupService.participateWorldCup();
    return ResponseEntity.ok(new SuccessResponse());
  }

  @Operation(summary = "월드컵 결과 넣기")
  @PostMapping("/results")
  ResponseEntity<SuccessResponse> postResult(@RequestBody @Valid WorldCupResultRequest worldCupResultRequest) {
    worldCupService.addWorldCupResult(worldCupResultRequest);
    return ResponseEntity.ok(new SuccessResponse());
  }

  @Operation(summary = "월드컵 결과 가져오기")
  @GetMapping("/results")
  ResponseEntity<List<WorldCupResultResponse>> getResult() {
    return ResponseEntity.ok(worldCupService.getWorldCupResults());
  }

}