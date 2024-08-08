package softeer.team_pineapple_be.global.common.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RandomUtilsTest {

    @Test
    @DisplayName("AuthCode가 원하는 범위로 반환되는 경우 - SuccessCase")
    public void getAuthCode_ShouldReturnSixDigitNumber() {
        // When
        Integer authCode = RandomUtils.getAuthCode();

        // Then
        assertThat(authCode)
                .isNotNull()
                .isGreaterThanOrEqualTo(100000)
                .isLessThan(1000000);
    }

    @Test
    @DisplayName("최댓값의 범위를 지정할 시 원하는 범위로 반환되는 경우 - SuccessCase")
    public void getSecureRandomNumberLessThan_ShouldReturnNumberLessThanMax() {
        // Given
        int max = 100;

        // When
        Integer randomNumber = RandomUtils.getSecureRandomNumberLessThen(max);

        // Then
        assertThat(randomNumber)
                .isNotNull()
                .isGreaterThanOrEqualTo(0)
                .isLessThan(max);
    }
}

