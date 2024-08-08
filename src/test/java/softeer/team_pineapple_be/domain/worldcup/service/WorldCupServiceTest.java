package softeer.team_pineapple_be.domain.worldcup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import softeer.team_pineapple_be.domain.draw.domain.DrawHistory;
import softeer.team_pineapple_be.domain.draw.domain.DrawRewardInfo;
import softeer.team_pineapple_be.domain.draw.exception.DrawErrorCode;
import softeer.team_pineapple_be.domain.draw.response.DrawLoseResponse;
import softeer.team_pineapple_be.domain.draw.response.DrawResponse;
import softeer.team_pineapple_be.domain.member.domain.Member;
import softeer.team_pineapple_be.domain.member.exception.MemberErrorCode;
import softeer.team_pineapple_be.domain.member.repository.MemberRepository;
import softeer.team_pineapple_be.domain.worldcup.request.WorldCupResultRequest;
import softeer.team_pineapple_be.domain.worldcup.response.WorldCupParticipateResponse;
import softeer.team_pineapple_be.domain.worldcup.response.WorldCupResultResponse;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;
import softeer.team_pineapple_be.global.exception.RestApiException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorldCupServiceTest {

    @InjectMocks
    private WorldCupService worldCupService;

    @Mock
    private WorldCupRedisService worldCupRedisService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthMemberService authMemberService;

    private Member member;
    private String phoneNumber;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        phoneNumber = "010-1234-5678";
        member = new Member(phoneNumber);
    }

    @Test
    public void testAddWorldCupResult() {
        // Arrange
        WorldCupResultRequest request = new WorldCupResultRequest(1); // 예시 ID

        // Act
        worldCupService.addWorldCupResult(request);

        // Assert
        verify(worldCupRedisService).increaseAnswerIdCount(1);
    }

    @Test
    public void testGetWorldCupResults() {
        // Arrange
        WorldCupResultResponse response1 = new WorldCupResultResponse(1, 10L, 30L);
        WorldCupResultResponse response2 = new WorldCupResultResponse(2, 20L, 70L);
        when(worldCupRedisService.getWorldCupResults()).thenReturn(Arrays.asList(response1, response2));

        // Act
        List<WorldCupResultResponse> results = worldCupService.getWorldCupResults();

        // Assert
        assertThat(results).hasSize(2)
                .extracting(WorldCupResultResponse::getId)
                .containsExactly(1, 2);
        assertThat(results).extracting(WorldCupResultResponse::getCount)
                .containsExactly(10L, 20L);
        assertThat(results).extracting(WorldCupResultResponse::getPercent)
                .containsExactly(30L, 70L);
    }


    @Test
    @DisplayName("멤버가 월드컵에 참여한 기록이 있는지 테스트 - SuccessCase")
    void isMemberParticipated_MemberExists_ReturnsWorldCupParticipateResponse() {
        // given
        when(authMemberService.getMemberPhoneNumber()).thenReturn(phoneNumber);
        when(memberRepository.findByPhoneNumber(phoneNumber)).thenReturn(java.util.Optional.of(member));

        // when
        WorldCupParticipateResponse response = worldCupService.isMemberParticipated();

        // then
        assertThat(response.getCar()).isFalse(); // AssertJ 사용
        verify(authMemberService).getMemberPhoneNumber();
        verify(memberRepository).findByPhoneNumber(phoneNumber);
    }

    @Test
    @DisplayName("멤버가 월드컵에 참여한 기록이 있는지 확인할 때 멤버가 없는 경우 테스트 - FailureCase")
    void isMemberParticipated_MemberDoesNotExist_ThrowsRestApiException() {
        // given
        when(authMemberService.getMemberPhoneNumber()).thenReturn(phoneNumber);
        when(memberRepository.findByPhoneNumber(phoneNumber)).thenReturn(java.util.Optional.empty());

        // when & then
        assertThatThrownBy(() -> worldCupService.isMemberParticipated()) // AssertJ 사용
                .isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(MemberErrorCode.NO_MEMBER);
                });

    }

    @Test
    @DisplayName("멤버가 월드컵 참여하는 경우 테스트 - SuccessCase")
    void participateWorldCup_MemberExists_ReturnsOk() {
        // given
        when(authMemberService.getMemberPhoneNumber()).thenReturn(phoneNumber);
        when(memberRepository.findByPhoneNumber(phoneNumber)).thenReturn(java.util.Optional.of(member));

        // when
        worldCupService.participateWorldCup();

        // then
        assertThat(member.isCar()).isTrue(); // AssertJ 사용
        verify(authMemberService).getMemberPhoneNumber();
        verify(memberRepository).findByPhoneNumber(phoneNumber);
    }

    @Test
    @DisplayName("멤버가 월드컵 참여할 떄 멤버가 없는 경우 테스트 - FailureCase")
    void participateWorldCup_MemberDoesNotExist_ThrowsRestApiException() {
        // given
        when(authMemberService.getMemberPhoneNumber()).thenReturn(phoneNumber);
        when(memberRepository.findByPhoneNumber(phoneNumber)).thenReturn(java.util.Optional.empty());

        // when & then
        assertThatThrownBy(() -> worldCupService.participateWorldCup()) // AssertJ 사용
                .isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(MemberErrorCode.NO_MEMBER);
                });
    }

}
