package softeer.team_pineapple_be.domain.draw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import softeer.team_pineapple_be.domain.draw.domain.DrawProbability;
import softeer.team_pineapple_be.domain.draw.repository.DrawProbabilityRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class RandomDrawPrizeServiceTest {

    @InjectMocks
    private RandomDrawPrizeService randomDrawPrizeService;

    @Mock
    private DrawProbabilityRepository drawProbabilityRepository;

    private List<DrawProbability> probabilityList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        randomDrawPrizeService.init(); // 초기화 메서드 호출
        probabilityList = Arrays.asList(
                new DrawProbability((byte) 1, 50), // 30%
                new DrawProbability((byte) 2, 30), // 50%
                new DrawProbability((byte) 3, 20)  // 20%
        );
    }

    @Test
    @DisplayName("경품 추첨시 등록된 순위에 대한 정보가 나오는지 테스트 - SuccessCase")
    void drawPrize_ReturnValidRanking() {
        // Given
        when(drawProbabilityRepository.findAll()).thenReturn(probabilityList);
        randomDrawPrizeService.init();

        // When
        Byte result = randomDrawPrizeService.drawPrize();

        // Then
        assertThat(result).isIn((byte) 1, (byte) 2, (byte) 3);
    }

    @Test
    @DisplayName("경품 추첨시 확률 정보가 신뢰할 수 있는지 테스트 - SuccessCase")
    void drawPrize_RespectProbabilities() {
        // Given
        when(drawProbabilityRepository.findAll()).thenReturn(probabilityList);
        randomDrawPrizeService.init();

        // When
        int[] count = new int[4];
        int iterations = 10000;

        for (int i = 0; i < iterations; i++) {
            Byte result = randomDrawPrizeService.drawPrize();
            count[result]++;
        }

        // Then
        assertThat(count[1]).isGreaterThan(count[2]);
        assertThat(count[2]).isGreaterThan(count[3]);
    }
}
