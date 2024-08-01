package softeer.team_pineapple_be.global.auth.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.global.exception.ErrorCode;

/**
 *
 */
@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

  NO_USER_INFO(HttpStatus.FORBIDDEN, "로그인이 되지 않았습니다"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
