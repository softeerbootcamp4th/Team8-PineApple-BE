package softeer.team_pineapple_be.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.validation.FieldError;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 에러 응답
 */
@Getter
@RequiredArgsConstructor
@Builder
public class ErrorResponse {
  private final String code;
  private final String message;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final List<ValidationError> errors;

  @Getter
  @Builder
  @RequiredArgsConstructor
  public static class ValidationError {

    private final String field;
    private final String message;

    public static ValidationError of(final FieldError fieldError) {
      return ValidationError.builder().field(fieldError.getField()).message(fieldError.getDefaultMessage()).build();
    }
  }
}
