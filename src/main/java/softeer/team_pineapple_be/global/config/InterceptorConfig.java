package softeer.team_pineapple_be.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.global.auth.interceptor.JwtInterceptor;

/**
 * 인터셉터 설정
 */
@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
  private final JwtInterceptor jwtInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtInterceptor);
  }
}
