//package softeer.team_pineapple_be.global.auth.interceptor;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.method.HandlerMethod;
//import softeer.team_pineapple_be.global.auth.annotation.Auth;
//import softeer.team_pineapple_be.global.auth.context.AuthContextHolder;
//import softeer.team_pineapple_be.global.auth.exception.AuthErrorCode;
//import softeer.team_pineapple_be.global.auth.utils.JwtUtils;
//import softeer.team_pineapple_be.global.exception.RestApiException;
//
//import java.lang.reflect.Method;
//
//public class JwtInterceptorTest {
//
//    private JwtInterceptor jwtInterceptor;
//
//    @Mock
//    private JwtUtils jwtUtils;
//
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @Mock
//    private HandlerMethod handlerMethod;
//
//    @Mock
//    private Object handler;
//
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        jwtInterceptor = new JwtInterceptor(jwtUtils);
//    }
//
//    @Test
//    public void preHandle_ShouldReturnTrue_WhenOptionsMethod() throws Exception {
//        // Given
//        when(request.getMethod()).thenReturn("OPTIONS");
//
//        // When
//        boolean result = jwtInterceptor.preHandle(request, response, new Object());
//
//        // Then
//        assertThat(result).isTrue();
//    }
//
//    @Test
//    public void preHandle_ShouldThrowException_WhenAuthorizationHeaderIsWrong() throws Exception {
//        // Given
//        when(request.getMethod()).thenReturn("GET");
//        when(handlerMethod.getMethodAnnotation(Auth.class)).thenReturn(mock(Auth.class));
//        when(request.getHeader("Authorization")).thenReturn("wrongToken");
//
//        // When & Then
//        assertThatThrownBy(() -> jwtInterceptor.preHandle(request, response, handlerMethod))
//                .isInstanceOf(RestApiException.class)
//                .satisfies(exception -> {
//                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
//                    assertThat(restApiException.getErrorCode()).isEqualTo(AuthErrorCode.JWT_PARSING_ERROR);
//                });
//    }
//
//    @Test
//    public void testPreHandle_withoutAuthAnnotation() throws Exception {
//        // Arrange
//        Method method = SampleController.class.getMethod("sampleMethod"); // SampleController의 샘플 메소드
//        HandlerMethod handlerMethod = new HandlerMethod(new SampleController(), method);
//        Object handler = handlerMethod;
//
//        // Auth 어노테이션이 없는 메소드로 설정
//        when(handlerMethod.getMethodAnnotation(Auth.class)).thenReturn(null);
//        when(handlerMethod.getBeanType().getAnnotation(Auth.class)).thenReturn(null);
//
//        // 요청 메소드가 OPTIONS가 아님
//        when(request.getMethod()).thenReturn("GET");
//
//        // Act
//        boolean result = jwtInterceptor.preHandle(request, response, handler);
//
//        // Assert
//        assertThat(result).isTrue() ;// true가 반환되어야 함
//    }
//
//    // 샘플 컨트롤러 클래스
//    public static class SampleController {
//        public void sampleMethod() {
//            // 빈 메소드
//        }
//    }
//    @Test
//    public void preHandle_ShouldThrowException_WhenAuthorizationHeaderIsMissing() throws Exception {
//        // Given
//        when(request.getMethod()).thenReturn("GET");
//        when(handlerMethod.getMethodAnnotation(Auth.class)).thenReturn(mock(Auth.class));
//        when(request.getHeader("Authorization")).thenReturn(null);
//
//        // When & Then
//        assertThatThrownBy(() -> jwtInterceptor.preHandle(request, response, handlerMethod))
//                .isInstanceOf(RestApiException.class)
//                .satisfies(exception -> {
//                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
//                    assertThat(restApiException.getErrorCode()).isEqualTo(AuthErrorCode.JWT_PARSING_ERROR);
//                });
//    }
//
//    @Test
//    public void preHandle_ShouldThrowException_WhenTokenIsInvalid() throws Exception {
//        // Given
//        String token = "invalid_token";
//        when(request.getMethod()).thenReturn("GET");
//        when(handlerMethod.getMethodAnnotation(Auth.class)).thenReturn(mock(Auth.class));
//        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
//
//        // When
//        when(jwtUtils.isExpired(token)).thenThrow(new RestApiException(AuthErrorCode.JWT_PARSING_ERROR));
//
//        // Then
//        assertThatThrownBy(() -> jwtInterceptor.preHandle(request, response, handlerMethod))
//                .isInstanceOf(RestApiException.class)
//                .satisfies(exception -> {
//                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
//                    assertThat(restApiException.getErrorCode()).isEqualTo(AuthErrorCode.JWT_PARSING_ERROR);
//                });
//    }
//
//    @Test
//    public void preHandle_ShouldSetAuthContext_WhenTokenIsValid() throws Exception {
//        // Given
//        String token = "valid_token";
//        String phoneNumber = "1234567890";
//        String role = "USER";
//
//        when(request.getMethod()).thenReturn("GET");
//        when(handlerMethod.getMethodAnnotation(Auth.class)).thenReturn(mock(Auth.class));
//        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
//        when(jwtUtils.getPhoneNumber(token)).thenReturn(phoneNumber);
//        when(jwtUtils.getRole(token)).thenReturn(role);
//        when(jwtUtils.isExpired(token)).thenReturn(false);
//
//        // When
//        boolean result = jwtInterceptor.preHandle(request, response, handlerMethod);
//
//        // Then
//        assertThat(result).isTrue();
//        assertThat(AuthContextHolder.getAuthContext().getPhoneNumber()).isEqualTo(phoneNumber);
//        assertThat(AuthContextHolder.getAuthContext().getRole()).isEqualTo(role);
//    }
//}
//
