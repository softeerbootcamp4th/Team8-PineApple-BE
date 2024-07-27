package softeer.team_pineapple_be.domain.quiz.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import softeer.team_pineapple_be.domain.quiz.response.QuizContentResponse;
import softeer.team_pineapple_be.domain.quiz.service.QuizContentService;

@Tag(name = "Quiz Content 제공", description = "날짜에 대한 퀴즈 정보를 제공하는 API")
@RestController
@RequiredArgsConstructor
public class QuizContentController {

    private final QuizContentService quizContentService;

    @GetMapping("/quiz")
    public ResponseEntity<QuizContentResponse> getQuizContent() {
        return ResponseEntity.ok().body(quizContentService.quizContent());
    }
}
