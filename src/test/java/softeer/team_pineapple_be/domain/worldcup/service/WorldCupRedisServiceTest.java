package softeer.team_pineapple_be.domain.worldcup.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import softeer.team_pineapple_be.domain.worldcup.response.WorldCupResultResponse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorldCupRedisServiceTest {

    @InjectMocks
    private WorldCupRedisService worldCupRedisService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
    }

    @Test
    public void testGetWorldCupResults() {
        // Arrange
        Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
        tupleSet.add(new TypedTupleImpl<>("1", 10.0));
        tupleSet.add(new TypedTupleImpl<>("2", 20.0));
        when(zSetOperations.rangeWithScores("worldcup_cnt", 0, -1)).thenReturn(tupleSet);

        // Act
        List<WorldCupResultResponse> results = worldCupRedisService.getWorldCupResults();

        // Assert
        assertThat(results).hasSize(2)
                .extracting(WorldCupResultResponse::getId)
                .containsExactly(2, 1);
        assertThat(results).extracting(WorldCupResultResponse::getCount)
                .containsExactly(20L, 10L);
    }

    @Test
    public void testIncreaseAnswerIdCount() {
        // Arrange
        Integer answerId = 1;

        // Act
        worldCupRedisService.increaseAnswerIdCount(answerId);

        // Assert
        verify(zSetOperations).incrementScore("worldcup_cnt", "1", 1);
    }

    // TypedTuple 구현체
    private static class TypedTupleImpl<T> implements ZSetOperations.TypedTuple<T>, Comparable<ZSetOperations.TypedTuple<T>> {
        private final T value;
        private final Double score;

        public TypedTupleImpl(T value, Double score) {
            this.value = value;
            this.score = score;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public Double getScore() {
            return score;
        }

        @Override
        public int compareTo(ZSetOperations.TypedTuple<T> o) {
            return this.score.compareTo(o.getScore());
        }
    }
}

