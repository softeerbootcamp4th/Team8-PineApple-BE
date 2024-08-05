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
  JWT_PARSING_ERROR(HttpStatus.FORBIDDEN, "JWT 토큰을 파싱할 수 없습니다."),
  JWT_EXPIRED(HttpStatus.FORBIDDEN, "JWT 토큰이 만료되었습니다");

  private final HttpStatus httpStatus;
  private final String message;
}
