package softeer.team_pineapple_be.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

/**
 * 단일 데이터 응답
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse {
  private final Object data;

  public DataResponse(Object data) {
    this.data = data;
  }
}
