package softeer.team_pineapple_be.domain.quiz.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import softeer.team_pineapple_be.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum QuizErrorCode implements ErrorCode {

    NO_QUIZ_INFO(HttpStatus.BAD_REQUEST, "퀴즈 정답이 등록되어 있지 않습니다."),
    NO_QUIZ_CONTENT(HttpStatus.BAD_REQUEST, "퀴즈 내용이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
