package softeer.team_pineapple_be.global.common.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * 성공 응답
 */
@Getter
public class SuccessResponse {
  private final int status = HttpStatus.OK.value();
  private final String message = "success";
}
