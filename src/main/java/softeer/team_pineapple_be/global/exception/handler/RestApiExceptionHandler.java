package softeer.team_pineapple_be.global.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import softeer.team_pineapple_be.global.common.response.ErrorResponse;
import softeer.team_pineapple_be.global.exception.CommonErrorCode;
import softeer.team_pineapple_be.global.exception.ErrorCode;
import softeer.team_pineapple_be.global.exception.RestApiException;

/**
 * API 에러처리
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * 설정 안한 모든 예외 처리
   *
   * @param ex
   */
  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handleAllException(Exception ex) {
    log.warn("handleAllException", ex);
    ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
    return handleExceptionInternal(errorCode);
  }

  /**
   * 사용자 지정 예외 처리
   *
   * @param e
   */
  @ExceptionHandler(RestApiException.class)
  public ResponseEntity<Object> handleCustomException(RestApiException e) {
    ErrorCode errorCode = e.getErrorCode();
    return handleExceptionInternal(errorCode);
  }

  /**
   * IllegalArgumentException 처리
   *
   * @param e
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
    log.warn("handleIllegalArgument", e);
    ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
    return handleExceptionInternal(errorCode, e.getMessage());
  }

  /**
   * Bean Validation 예외처리
   *
   * @param e
   * @param headers
   * @param statusCode
   * @param request
   */
  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
      HttpStatusCode statusCode, WebRequest request) {
    log.warn("handleIllegalArgument", e);
    ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
    return handleExceptionInternal(e, errorCode);
  }

  /**
   * 사용자지정, 기타 예외 내부 처리
   *
   * @param errorCode
   */
  private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.getHttpStatus()).body(makeErrorResponse(errorCode));
  }

  /**
   * IllegalArgumentException 내부 처리
   *
   * @param errorCode
   * @param message
   */
  private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
    return ResponseEntity.status(errorCode.getHttpStatus()).body(makeErrorResponse(errorCode, message));
  }

  /**
   * Validation 예외 내부 처리
   *
   * @param e
   * @param errorCode
   */
  private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.getHttpStatus()).body(makeErrorResponse(e, errorCode));
  }

  private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
    return ErrorResponse.builder().code(errorCode.name()).message(message).build();
  }

  private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
    return ErrorResponse.builder().code(errorCode.name()).message(errorCode.getMessage()).build();
  }

  private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
    List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                                                               .getFieldErrors()
                                                               .stream()
                                                               .map(ErrorResponse.ValidationError::of)
                                                               .collect(Collectors.toList());

    return ErrorResponse.builder()
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .errors(validationErrorList)
                        .build();
  }
}
