package softeer.team_pineapple_be.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import softeer.team_pineapple_be.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum MemberAuthorizationErrorCode implements ErrorCode {

    CODE_NOT_SENT(HttpStatus.BAD_REQUEST,"인증번호가 제대로 전송되지 않았습니다."),
    CODE_EXPIRED(HttpStatus.BAD_REQUEST,"인증번호 유효시간이 초과되었습니다."),
    CODE_INCORRECT(HttpStatus.BAD_REQUEST,"인증번호가 틀렸습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
