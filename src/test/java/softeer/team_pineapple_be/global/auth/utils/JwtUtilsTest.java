package softeer.team_pineapple_be.global.auth.utils;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import softeer.team_pineapple_be.global.auth.exception.AuthErrorCode;
import softeer.team_pineapple_be.global.exception.RestApiException;

public class JwtUtilsTest {


    private JwtUtils jwtUtils;
    private String secret = "this_is_a_test_secure_secret_key_1234";
    private String token;
    private String invalidToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils = new JwtUtils(secret);
        token = jwtUtils.createJwt("testCategory", "010-1234-5678", "USER", 1000L);
        invalidToken = "invalid.token";
    }

    @Test
    @DisplayName("성공적으로 jwt토큰을 발급하여 반환하는 경우 - SuccessCase")
    public void createJwt_ReturnsValidToken() {
        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("성공적으로 jwt토큰에서 카테고리를 추출하여 반환하는 경우 - SuccessCase")
    public void getCategory_ReturnsCategoryFromToken() {
        // When
        String category = jwtUtils.getCategory(token);

        // Then
        assertThat(category).isEqualTo("testCategory");
    }

    @Test
    @DisplayName("카테고리를 추출하려고 했으나 토큰이 유효하지않은 경우 - FailureCase")
    public void getCategory_JwtParsingFails_ThrowsRestApiException() {
        // When & Then
        assertThatThrownBy(() -> jwtUtils.getCategory(invalidToken))
                .isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(AuthErrorCode.JWT_PARSING_ERROR);
                });
    }

    @Test
    @DisplayName("성공적으로 jwt토큰에서 전화번호를 추출하여 반환하는 경우 - SuccessCase")
    public void getPhoneNumber_ReturnsPhoneNumberFromToken() {
        // When
        String phoneNumber = jwtUtils.getPhoneNumber(token);

        // Then
        assertThat(phoneNumber).isEqualTo("010-1234-5678");
    }

    @Test
    @DisplayName("전화번호를 추출하려고 했으나 토큰이 유효하지않은 경우 - FailureCase")
    public void getPhoneNumber_JwtParsingFails_ThrowsRestApiException() {
        // When & Then
        assertThatThrownBy(() -> jwtUtils.getPhoneNumber(invalidToken))
                .isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(AuthErrorCode.JWT_PARSING_ERROR);
                });
    }

    @Test
    @DisplayName("jwt토큰이 만료되어 false가 반환된 경우 - SuccessCase")
    public void isExpired_NotExpired_ReturnsFalse() {
        // When
        Boolean isExpired = jwtUtils.isExpired(token);

        // Then
        assertThat(isExpired).isFalse();
    }

    @Test
    @DisplayName("성공적으로 jwt토큰에서 유저역할을 추출하여 반환하는 경우 - SuccessCase")
    public void getRole_ReturnsRoleFromToken() {
        // When
        String role = jwtUtils.getRole(token);

        // Then
        assertThat(role).isEqualTo("USER");
    }

    @Test
    @DisplayName("유저역할 추출하려고 했으나 토큰이 유효하지않은 경우 - FailureCase")
    public void getRole_JwtParsingFails_ThrowsRestApiException() {
        // When & Then
        assertThatThrownBy(() -> jwtUtils.getRole(invalidToken))
                .isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(AuthErrorCode.JWT_PARSING_ERROR);
                });
    }

    @Test
    @DisplayName("토큰이 만료되어 예외가 발생되는 경우 - FailureCase")
    public void isExpired_TokenIsExpired_ThrowsRestApiException() {
       // When & Then
        assertThatThrownBy(() -> jwtUtils.isExpired(invalidToken))
                .isInstanceOf(RestApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", AuthErrorCode.JWT_EXPIRED);
    }
}

