package softeer.team_pineapple_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class TeamPineappleBeApplication {

  public static void main(String[] args) {
    SpringApplication.run(TeamPineappleBeApplication.class, args);
  }

}
