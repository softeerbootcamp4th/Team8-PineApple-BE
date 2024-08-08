package softeer.team_pineapple_be.global.auth.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

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
    if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
      return true;
    }
    String authorization = request.getHeader("Authorization");
    if (!checkAnnotation(handler, Auth.class)) {
      if (authorization == null) {
        return true;
      }
      saveContext(authorization);
      return true;
    }
    return authorizeAndSaveContext(authorization);
  }

  /**
   * 인증하고 컨텍스트 저장하는 메서드
   *
   * @param authorization
   * @return 인증 성공 여부
   */
  private boolean authorizeAndSaveContext(String authorization) {
    String token;
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      throw new RestApiException(AuthErrorCode.JWT_PARSING_ERROR);
    }
    token = authorization.substring(7);
    jwtUtils.isExpired(token);
    AuthContextHolder.setAuthContext(new AuthContext(jwtUtils.getPhoneNumber(token), jwtUtils.getRole(token)));
    return true;
  }

  /**
   * Auth 어노테이션 존재하는지 확인하는 메소드
   *
   * @param handler
   * @param authClass
   * @return
   */
  private boolean checkAnnotation(Object handler, Class<Auth> authClass) {
    if (handler instanceof ResourceHttpRequestHandler) {
      return false;
    }

    HandlerMethod handlerMethod = (HandlerMethod) handler;
    if (null != handlerMethod.getMethodAnnotation(authClass) ||
        null != handlerMethod.getBeanType().getAnnotation(authClass)) {
      return true;
    }
    return false;
  }

  /**
   * 굳이 인증이 필요 없는데 컨텍스트는 저장하는 메소드
   *
   * @param authorization
   */
  private void saveContext(String authorization) {
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      return;
    }
    String token = authorization.substring(7);
    try {
      jwtUtils.isExpired(token);
    } catch (Exception e) {
      return;
    }
    AuthContextHolder.setAuthContext(new AuthContext(jwtUtils.getPhoneNumber(token), jwtUtils.getRole(token)));
  }
}
