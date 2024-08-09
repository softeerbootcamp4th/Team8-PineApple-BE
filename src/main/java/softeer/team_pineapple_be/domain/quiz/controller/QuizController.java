package softeer.team_pineapple_be.domain.quiz.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.member.response.MemberInfoResponse;
import softeer.team_pineapple_be.domain.quiz.request.QuizInfoRequest;
import softeer.team_pineapple_be.domain.quiz.request.QuizRewardRequest;
import softeer.team_pineapple_be.domain.quiz.response.QuizContentResponse;
import softeer.team_pineapple_be.domain.quiz.response.QuizInfoResponse;
import softeer.team_pineapple_be.domain.quiz.service.QuizService;
import softeer.team_pineapple_be.global.auth.annotation.Auth;
import softeer.team_pineapple_be.global.common.response.SuccessResponse;

@Tag(name = "Quiz 관련 정보 제공", description = "퀴즈에 대한 처리(내용, 정답)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

  private final QuizService quizService;

  @Operation(summary = "퀴즈 내용 가져오기")
  @GetMapping
  public ResponseEntity<QuizContentResponse> getQuizContent() {
    return ResponseEntity.ok().body(quizService.getQuizContent());
  }

  @Auth
  @Operation(summary = "퀴즈 선착순 경품 받기")
  @PostMapping("/reward")
  public ResponseEntity<SuccessResponse> getQuizReward(@RequestBody @Valid QuizRewardRequest quizRewardRequest) {
    quizService.getQuizReward(quizRewardRequest.getParticipantId());
    return ResponseEntity.ok(new SuccessResponse());
  }

  @Operation(summary = "퀴즈 정답 맞추기")
  @PostMapping("/answer")
  public ResponseEntity<QuizInfoResponse> isCorrect(@Valid @RequestBody QuizInfoRequest quizInfoRequest) {
    return ResponseEntity.ok().body(quizService.quizIsCorrect(quizInfoRequest));
  }

  @Auth
  @Operation(summary = "퀴즈 참여 여부 등록")
  @GetMapping("/participants")
  public ResponseEntity<MemberInfoResponse> setQuizHistory() {
    return ResponseEntity.ok().body(quizService.quizHistory());
  }
}
