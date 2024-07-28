package softeer.team_pineapple_be.domain.quiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softeer.team_pineapple_be.domain.quiz.request.QuizInfoRequest;
import softeer.team_pineapple_be.domain.quiz.response.QuizContentResponse;
import softeer.team_pineapple_be.domain.quiz.response.QuizInfoResponse;
import softeer.team_pineapple_be.domain.quiz.service.QuizService;

@Tag(name = "Quiz 관련 정보 제공", description = "퀴즈에 대한 처리(내용, 정답)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "퀴즈 내용 가져오기")
    @GetMapping
    public ResponseEntity<QuizContentResponse> getQuizContent() {
        return ResponseEntity.ok().body(quizService.quizContent());
    }

    @Operation(summary = "퀴즈 정답 맞추기")
    @PostMapping("/answer")
    public ResponseEntity<QuizInfoResponse> isCorrect(@RequestBody QuizInfoRequest quizInfoRequest) {
        return ResponseEntity.ok().body(quizService.quizIsCorrect(quizInfoRequest));
    }
}
