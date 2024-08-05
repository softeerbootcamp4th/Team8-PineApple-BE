package softeer.team_pineapple_be.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import softeer.team_pineapple_be.global.auth.filter.ThreadLocalCleanerFilter;

/**
 * 필터 설정
 */
@Configuration
public class FilterConfig {
  @Bean
  public FilterRegistrationBean<ThreadLocalCleanerFilter> contextCleanerFilterRegistrationBean() {
    FilterRegistrationBean<ThreadLocalCleanerFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new ThreadLocalCleanerFilter());
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }
}
