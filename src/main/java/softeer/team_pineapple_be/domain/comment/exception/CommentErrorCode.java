package softeer.team_pineapple_be.domain.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import softeer.team_pineapple_be.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

    ALREADY_REVIEWED(HttpStatus.BAD_REQUEST, "이미 기대평을 남긴 사용자입니다."),
    NO_COMMENT(HttpStatus.BAD_REQUEST, "존재하지 않는 기대평입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
