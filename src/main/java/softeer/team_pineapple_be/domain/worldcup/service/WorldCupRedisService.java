package softeer.team_pineapple_be.domain.worldcup.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.worldcup.response.WorldCupResultResponse;

/**
 * 월드컵 레디스 서비스
 */
@RequiredArgsConstructor
@Service
public class WorldCupRedisService {
  private final static int WORLD_CUP_ANSWER_ID_START = 1;
  private final static int WORLD_CUP_ANSWER_ID_END = 6;
  private final static String ANSWER_KEY = "worldcup_cnt";
  private final RedisTemplate<String, String> redisTemplate;

  /**
   * 유저들의 월드컵 통계를 가져오는 메서드
   *
   * @return
   */
  public List<WorldCupResultResponse> getWorldCupResults() {
    Set<ZSetOperations.TypedTuple<String>> tupleSet =
        redisTemplate.opsForZSet().reverseRangeWithScores(ANSWER_KEY, 0, -1);
    long totalCount = tupleSet.stream().mapToLong(typedTuple -> typedTuple.getScore().longValue()).sum();
    List<WorldCupResultResponse> worldCupResultResponses = new ArrayList<>();
    tupleSet.forEach(typedTuple -> {
      long count = typedTuple.getScore().longValue();
      int answerId = Integer.parseInt(typedTuple.getValue());
      worldCupResultResponses.add(WorldCupResultResponse.from(answerId, count, totalCount));
    });
    return worldCupResultResponses;
  }

  /**
   * 월드컵 답안의 선택 수를 증가시키는 메서드
   *
   * @param worldCupAnswerId
   */
  public void increaseAnswerIdCount(Integer worldCupAnswerId) {
    redisTemplate.opsForZSet().incrementScore(ANSWER_KEY, String.valueOf(worldCupAnswerId), 1);
  }

  /**
   * 월드컵 통계 초기화 -> 아직 투표를 못받은 아이템을 0으로 초기화
   */
  @PostConstruct
  public void init() {
    ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
    for (int i = WORLD_CUP_ANSWER_ID_START; i <= WORLD_CUP_ANSWER_ID_END; i++) {
      zSetOps.addIfAbsent(ANSWER_KEY, String.valueOf(i), 0);
    }
  }
}
