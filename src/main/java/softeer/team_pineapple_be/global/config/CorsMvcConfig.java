package softeer.team_pineapple_be.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry corsRegistry) {

    corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
  }
}