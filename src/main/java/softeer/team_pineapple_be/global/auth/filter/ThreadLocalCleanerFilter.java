package softeer.team_pineapple_be.global.auth.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import softeer.team_pineapple_be.global.auth.context.AuthContextHolder;

/**
 * 쓰레드 로컬 지우는 필터
 */
@Slf4j
public class ThreadLocalCleanerFilter implements Filter {

  @Override
  public void destroy() {
    Filter.super.destroy();
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    try {
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      AuthContextHolder.clearContext();
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }
}
