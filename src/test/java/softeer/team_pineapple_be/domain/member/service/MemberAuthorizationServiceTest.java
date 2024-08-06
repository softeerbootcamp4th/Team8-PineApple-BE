package softeer.team_pineapple_be.domain.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
import java.util.Optional;

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
    @DisplayName("토큰을 이용한 로그인 요청이 성공한 경우 - SuccessCase")
    void loginWithAuthCode_Success_ReturnsMemberLoginInfoResponse() {
        // given
        when(memberAuthorizationRepository.findByPhoneNumber(phoneNumber)).thenReturn(memberAuthorization);
        when(memberRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(member));
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
    @DisplayName("토큰을 이용한 로그인 요청이 성공한 경우 - SuccessCase")
    void loginWithAuthCode_MemberNotExists_Success_ReturnsMemberLoginInfoResponse() {
        // given
        when(memberAuthorizationRepository.findByPhoneNumber(phoneNumber)).thenReturn(memberAuthorization);
        when(memberRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(new Member(phoneNumber));
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
    @DisplayName("토큰을 이용한 로그인 요청시도했으나 코드가 전송되지 않은 경우 - FailureCase")
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

    @Test
    @DisplayName("토큰을 이용한 로그인 요청시도했으나 코드가 만료된 경우 - FailureCase")
    void loginWithAuthCode_CodeExpired_ThrowsException() {
        // given
        when(memberAuthorizationRepository.findByPhoneNumber(phoneNumber)).thenReturn(memberAuthorization);
        when(memberRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(member));


        LocalDateTime expiredTime = LocalDateTime.now().plusDays(10);

        // MockedStatic을 사용하여 LocalDateTime.now() 메소드를 모킹
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(expiredTime);

            // when & then
            assertThatThrownBy(() -> memberAuthorizationService.loginWithAuthCode(phoneNumber, authCode))
                    .isInstanceOf(RestApiException.class)
                    .satisfies(exception -> {
                        RestApiException restApiException = (RestApiException) exception; // 캐스팅
                        assertThat(restApiException.getErrorCode()).isEqualTo(MemberAuthorizationErrorCode.CODE_EXPIRED);
                    });
        }
    }

    @Test
    @DisplayName("토큰을 이용한 로그인 요청시도했으나 코드가 부정확한 경우 - FailureCase")
    void loginWithAuthCode_CodeIncorrect_ThrowsException() {
        // given
        when(memberAuthorizationRepository.findByPhoneNumber(phoneNumber)).thenReturn(memberAuthorization);

        // when & then
        assertThatThrownBy(() -> memberAuthorizationService.loginWithAuthCode(phoneNumber, 654321))
                .isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(MemberAuthorizationErrorCode.CODE_INCORRECT);
                });
    }

    @Test
    @DisplayName("핸드폰 번호를 이용한 인증 요청 시 인증번호가 제대로 전송된 경우 - SuccessCase")
    void loginWithPhoneNumber_Success_SavesAuthorizationCode() {
        // given
        when(phoneAuthorizationService.sendAuthMessage(phoneNumber)).thenReturn(authCode);
        when(memberAuthorizationRepository.findByPhoneNumber(phoneNumber)).thenReturn(null);

        // when
        memberAuthorizationService.loginWithPhoneNumber(phoneNumber);

        // then
        verify(phoneAuthorizationService).sendAuthMessage(phoneNumber);
        verify(memberAuthorizationRepository).save(any(MemberAuthorization.class));
    }

    @Test
    @DisplayName("핸드폰 번호를 이용한 인증 요청 시 인증이 제대로 되었으며 이미 존재하는 멤버가 있는 경우 - SuccessCase")
    void loginWithPhoneNumber_UpdatesAuthorizationCode_WhenExists() {
        // given
        Integer newAuthCode = 654321; // 새로운 인증 코드
        MemberAuthorization existingAuthorization = new MemberAuthorization(phoneNumber, 123456); // 기존 인증 정보 객체 생성

        // Mock 설정
        when(phoneAuthorizationService.sendAuthMessage(phoneNumber)).thenReturn(newAuthCode);
        when(memberAuthorizationRepository.findByPhoneNumber(phoneNumber)).thenReturn(existingAuthorization); // 기존 인증 정보 반환

        // when
        memberAuthorizationService.loginWithPhoneNumber(phoneNumber);

        // then
        // 기존 객체의 인증 코드가 업데이트되었는지 확인
        assertThat(existingAuthorization.getAuthorizationCode()).isEqualTo(newAuthCode);
        // 코드 만료 시간도 업데이트되었는지 확인
        assertThat(existingAuthorization.getCodeExpireTime()).isAfter(LocalDateTime.now());

        // save가 호출되지 않았는지 확인
        verify(memberAuthorizationRepository, never()).save(any());

    }

}
