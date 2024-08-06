package softeer.team_pineapple_be.domain.draw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import softeer.team_pineapple_be.domain.draw.domain.DrawDailyMessageInfo;
import softeer.team_pineapple_be.domain.draw.domain.DrawHistory;
import softeer.team_pineapple_be.domain.draw.domain.DrawPrize;
import softeer.team_pineapple_be.domain.draw.domain.DrawRewardInfo;
import softeer.team_pineapple_be.domain.draw.exception.DrawErrorCode;
import softeer.team_pineapple_be.domain.draw.repository.DrawDailyMessageInfoRepository;
import softeer.team_pineapple_be.domain.draw.repository.DrawHistoryRepository;
import softeer.team_pineapple_be.domain.draw.repository.DrawPrizeRepository;
import softeer.team_pineapple_be.domain.draw.repository.DrawRewardInfoRepository;
import softeer.team_pineapple_be.domain.draw.response.DrawLoseResponse;
import softeer.team_pineapple_be.domain.draw.response.DrawResponse;
import softeer.team_pineapple_be.domain.draw.response.DrawWinningResponse;
import softeer.team_pineapple_be.domain.member.domain.Member;
import softeer.team_pineapple_be.domain.member.exception.MemberErrorCode;
import softeer.team_pineapple_be.domain.member.repository.MemberRepository;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;
import softeer.team_pineapple_be.global.exception.RestApiException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DrawServiceTest {

    @InjectMocks
    private DrawService drawService;

    @Mock
    private DrawDailyMessageInfoRepository drawDailyMessageInfoRepository;

    @Mock
    private DrawHistoryRepository drawHistoryRepository;

    @Mock
    private DrawPrizeRepository drawPrizeRepository;

    @Mock
    private DrawRewardInfoRepository drawRewardInfoRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RandomDrawPrizeService randomDrawPrizeService;

    @Mock
    private AuthMemberService authMemberService;

    private String phoneNumber;
    private Byte prizeRank;
    private DrawDailyMessageInfo drawDailyMessageInfo;
    private DrawPrize drawPrize;
    private List<DrawPrize> drawPrizeList; // 값이 들어간 리스트
    private DrawRewardInfo rewardInfo ; // 값이 들어간 리스트로 초기화

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        phoneNumber = "010-1234-5678";
        prizeRank = 1;
        drawDailyMessageInfo = new DrawDailyMessageInfo(1, "Win message", "Lose message", "Lose scenario", "Win image", "Lose image", LocalDate.now());
        drawPrize = new DrawPrize(1L, "prize_image_url", true, phoneNumber, null);
        drawPrizeList = new ArrayList<>(List.of(drawPrize));
        rewardInfo = new DrawRewardInfo(prizeRank, "Prize", 1, drawPrizeList);
    }

    @Test
    @DisplayName("사용자가 응모에 성공적으로 참여한 케이스 테스트 - SuccessCase")
    void enterDraw_DrawIsSuccessful_ReturnWinningResponse() {
        // Given
        Member member = new Member(phoneNumber);
        member.incrementToolBoxCnt(); // 툴박스 개수 증가
        member.generateCar(); // 차량 보유 설정

        when(authMemberService.getMemberPhoneNumber()).thenReturn(phoneNumber);
        when(memberRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(member));
        when(randomDrawPrizeService.drawPrize()).thenReturn(prizeRank);
        when(drawRewardInfoRepository.findById(prizeRank)).thenReturn(Optional.of(rewardInfo));
        when(drawDailyMessageInfoRepository.findByDrawDate(LocalDate.now())).thenReturn(Optional.of(drawDailyMessageInfo));
        when(drawPrizeRepository.findFirstByDrawRewardInfoAndValid(rewardInfo, true)).thenReturn(Optional.of(drawPrize));

        // When
        DrawResponse response = drawService.enterDraw();

        // Then
        verify(drawHistoryRepository).save(any(DrawHistory.class));
        assertThat(rewardInfo.getStock()).isEqualTo(0); // 재고 감소 확인
        assert response instanceof DrawWinningResponse;
    }

    @Test
    @DisplayName("사용자가 응모에 성공적으로 참여했으나 경품 재고가 없어 당첨에 실패한 케이스 - SuccessCase")
    void enterDraw_PrizeIsNotAvailable_ReturnLoseResponse() {
        // Given
        Member member = new Member(phoneNumber);
        member.incrementToolBoxCnt(); // 툴박스 개수 증가
        member.generateCar(); // 차량 보유 설정

        when(authMemberService.getMemberPhoneNumber()).thenReturn(phoneNumber);
        when(memberRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(member));
        when(randomDrawPrizeService.drawPrize()).thenReturn(prizeRank);
        when(drawRewardInfoRepository.findById(prizeRank)).thenReturn(Optional.of(new DrawRewardInfo(prizeRank, "Prize", 0, null))); // 재고 없음
        when(drawDailyMessageInfoRepository.findByDrawDate(LocalDate.now())).thenReturn(Optional.of(drawDailyMessageInfo));

        // When
        DrawResponse response = drawService.enterDraw();

        // Then
        verify(drawHistoryRepository).save(any(DrawHistory.class));
        assert response instanceof DrawLoseResponse;
    }

    @Test
    @DisplayName("사용자가 참여하려고 했으나 존재하지 않는 멤버인 케이스 - FailureCase")
    void enterDraw_MemberNotFound_ThrowException() {
        // Given
        when(authMemberService.getMemberPhoneNumber()).thenReturn(phoneNumber);
        when(memberRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> drawService.enterDraw())
                .isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(MemberErrorCode.NO_MEMBER);
                });
    }

}
