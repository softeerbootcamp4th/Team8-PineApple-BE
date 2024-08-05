package softeer.team_pineapple_be.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import softeer.team_pineapple_be.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    NO_MEMBER(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
