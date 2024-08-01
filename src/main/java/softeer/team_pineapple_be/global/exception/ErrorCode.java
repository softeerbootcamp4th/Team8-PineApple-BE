package softeer.team_pineapple_be.global.exception;

import org.springframework.http.HttpStatus;

/**
 * 에러코드 인터페이스
 */
public interface ErrorCode {
  HttpStatus getHttpStatus();

  String getMessage();

  String name();
}
