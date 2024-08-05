package softeer.team_pineapple_be.domain.worldcup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import softeer.team_pineapple_be.domain.member.domain.Member;
import softeer.team_pineapple_be.domain.member.exception.MemberErrorCode;
import softeer.team_pineapple_be.domain.member.repository.MemberRepository;
import softeer.team_pineapple_be.domain.worldcup.response.WorldCupParticipateResponse;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;
import softeer.team_pineapple_be.global.exception.RestApiException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorldCupServiceTest {

    @InjectMocks
    private WorldCupService worldCupService;

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
    void isMemberParticipated_MemberDoesNotExist_ThrowsException() {
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
    void participateWorldCup_MemberDoesNotExist_ThrowsRestApiException() {
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
}
