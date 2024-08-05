package softeer.team_pineapple_be.domain.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import softeer.team_pineapple_be.domain.member.domain.Member;
import softeer.team_pineapple_be.domain.member.domain.MemberAuthorization;
import softeer.team_pineapple_be.domain.member.exception.MemberAuthorizationErrorCode;
import softeer.team_pineapple_be.domain.member.repository.MemberAuthorizationRepository;
import softeer.team_pineapple_be.domain.member.repository.MemberRepository;
import softeer.team_pineapple_be.domain.member.response.MemberLoginInfoResponse;
import softeer.team_pineapple_be.global.auth.service.PhoneAuthorizationService;
import softeer.team_pineapple_be.global.auth.utils.JwtUtils;
import softeer.team_pineapple_be.global.exception.RestApiException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemberAuthorizationServiceTest {

    @InjectMocks
    private MemberAuthorizationService memberAuthorizationService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PhoneAuthorizationService phoneAuthorizationService;

    @Mock
    private MemberAuthorizationRepository memberAuthorizationRepository;

    @Mock
    private JwtUtils jwtUtils;

    private String phoneNumber;
    private Integer authCode;
    private MemberAuthorization memberAuthorization;
    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        phoneNumber = "010-1234-5678";
        authCode = 123456;
        memberAuthorization = new MemberAuthorization(phoneNumber, authCode);
        member = new Member(phoneNumber);
    }

    @Test
    void loginWithAuthCode_Success_ReturnsMemberLoginInfoResponse() {
        // given
        when(memberAuthorizationRepository.findByPhoneNumber(phoneNumber)).thenReturn(memberAuthorization);
        when(memberRepository.findByPhoneNumber(phoneNumber)).thenReturn(java.util.Optional.of(member));
        when(jwtUtils.createJwt(anyString(), anyString(), anyString(), anyLong())).thenReturn("mockedToken");

        // when
        MemberLoginInfoResponse response = memberAuthorizationService.loginWithAuthCode(phoneNumber, authCode);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(response.getToolBoxCnt()).isEqualTo(0);
        assertThat(response.isCar()).isEqualTo(false);
        assertThat(response.getAccessToken()).isEqualTo("mockedToken");
        verify(memberAuthorizationRepository).findByPhoneNumber(phoneNumber);
        verify(memberRepository).findByPhoneNumber(phoneNumber);
        verify(jwtUtils).createJwt("access_token", phoneNumber, member.getRole(), 2 * 24 * 60 * 60 * 1000L);
    }

    @Test
    void loginWithAuthCode_CodeNotSent_ThrowsException() {
        // given
        when(memberAuthorizationRepository.findByPhoneNumber(phoneNumber)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> memberAuthorizationService.loginWithAuthCode(phoneNumber, authCode))
                .isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(MemberAuthorizationErrorCode.CODE_NOT_SENT);
                });
    }

}
