package softeer.team_pineapple_be.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * Lua 스크립트 설정
 */
@Configuration
public class LuaScriptConfig {
  @Bean
  public RedisScript<Long> firstComeFirstServeScript() {
    ClassPathResource fcfsScript = new ClassPathResource("redis-script/fcfs-script.lua");
    return RedisScript.of(fcfsScript, Long.class);
  }
}
