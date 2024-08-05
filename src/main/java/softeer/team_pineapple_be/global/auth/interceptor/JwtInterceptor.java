package softeer.team_pineapple_be.global.auth.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.global.auth.annotation.Auth;
import softeer.team_pineapple_be.global.auth.context.AuthContext;
import softeer.team_pineapple_be.global.auth.context.AuthContextHolder;
import softeer.team_pineapple_be.global.auth.exception.AuthErrorCode;
import softeer.team_pineapple_be.global.auth.utils.JwtUtils;
import softeer.team_pineapple_be.global.exception.RestApiException;

/**
 * JWT 인증 인터셉터
 */
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
  private final JwtUtils jwtUtils;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (!checkAnnotation(handler, Auth.class)) {
      return true;
    }
    String authorization = request.getHeader("Authorization");
    String token;
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      throw new RestApiException(AuthErrorCode.JWT_PARSING_ERROR);
    }
    token = authorization.substring(7);
    jwtUtils.isExpired(token);
    AuthContextHolder.setAuthContext(new AuthContext(jwtUtils.getPhoneNumber(token), jwtUtils.getRole(token)));
    return true;
  }

  private boolean checkAnnotation(Object handler, Class<Auth> authClass) {
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    if (null != handlerMethod.getMethodAnnotation(authClass) ||
        null != handlerMethod.getBeanType().getAnnotation(authClass)) {
      return true;
    }
    return false;
  }
}
