package softeer.team_pineapple_be.domain.fcfs.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.global.exception.ErrorCode;

/**
 * 선착순 기능 에러 코드
 */
@Getter
@RequiredArgsConstructor
public enum FcfsErrorCode implements ErrorCode {
  NOT_FOR_REWARD(HttpStatus.BAD_REQUEST, "선착순 경품의 대상자가 아닙니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
