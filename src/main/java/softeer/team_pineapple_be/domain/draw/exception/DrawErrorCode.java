package softeer.team_pineapple_be.domain.draw.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import softeer.team_pineapple_be.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum DrawErrorCode implements ErrorCode {

    NO_PRIZE(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다."),
    CANNOT_ENTER_DRAW(HttpStatus.BAD_REQUEST, "상품 응모 조건에 부합하지 않습니다."),
    NO_VALID_PRIZE(HttpStatus.BAD_REQUEST, "상품의 개수와 유효성이 일치하지 않습니다."),
    NOT_PRIZE_OWNER(HttpStatus.BAD_REQUEST, " 해당 상품의 당첨자가 아닙니다."),
    NOT_VALID_DATE(HttpStatus.BAD_REQUEST, "상품 추첨이 가능하지 않은 날짜입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
