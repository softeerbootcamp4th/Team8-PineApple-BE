package softeer.team_pineapple_be.domain.draw.service;

import org.springframework.stereotype.Service;

import java.util.List;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.draw.domain.DrawProbability;
import softeer.team_pineapple_be.domain.draw.repository.DrawProbabilityRepository;
import softeer.team_pineapple_be.global.common.utils.RandomUtils;

/**
 * 랜덤 추첨 서비스
 */
@Service
@RequiredArgsConstructor
public class RandomDrawPrizeService {
  private final DrawProbabilityRepository drawProbabilityRepository;
  private byte[] probabilityArray;

  /**
   * 랜덤 숫자로 경품 선택
   *
   * @return 경품 랭크
   */
  public Byte drawPrize() {
    Integer randomIndex = RandomUtils.getSecureRandomNumberLessThen(probabilityArray.length);
    return probabilityArray[randomIndex];
  }

  /**
   * 추첨 배열 초기화
   */
  @PostConstruct
  public void init() {
    List<DrawProbability> probabilityList = drawProbabilityRepository.findAll();
    int probabilitySum = 0;
    for (DrawProbability drawProbability : probabilityList) {
      probabilitySum += drawProbability.getProbability();
    }
    probabilityArray = new byte[probabilitySum];
    int index = 0;
    for (DrawProbability drawProbability : probabilityList) {
      Integer probability = drawProbability.getProbability();
      for (int i = 0; i < probability; i++) {
        probabilityArray[index++] = drawProbability.getRanking();
      }
    }
  }
}
