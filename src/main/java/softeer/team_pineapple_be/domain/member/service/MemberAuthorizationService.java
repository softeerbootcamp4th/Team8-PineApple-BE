package softeer.team_pineapple_be.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.member.domain.Member;
import softeer.team_pineapple_be.domain.member.domain.MemberAuthorization;
import softeer.team_pineapple_be.domain.member.exception.MemberAuthorizationErrorCode;
import softeer.team_pineapple_be.domain.member.repository.MemberAuthorizationRepository;
import softeer.team_pineapple_be.domain.member.repository.MemberRepository;
import softeer.team_pineapple_be.domain.member.response.MemberLoginInfoResponse;
import softeer.team_pineapple_be.global.auth.service.PhoneAuthorizationService;
import softeer.team_pineapple_be.global.auth.utils.JwtUtils;
import softeer.team_pineapple_be.global.exception.RestApiException;

/**
 * 멤버 로그인 인증번호 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberAuthorizationService {
  private final MemberRepository memberRepository;
  private final PhoneAuthorizationService phoneAuthorizationService;
  private final MemberAuthorizationRepository memberAuthorizationRepository;
  private final JwtUtils jwtUtils;

  /**
   * 인증코드와 전화번호를 받아서 올바른 인증코드를 입력했는지 확인하고 멤버정보 보냄.
   *
   * @param phoneNumber
   * @param authCode
   * @return MemberInfoResponse
   */
  @Transactional
  public MemberLoginInfoResponse loginWithAuthCode(String phoneNumber, Integer authCode) {
    MemberAuthorization memberAuthorization = memberAuthorizationRepository.findByPhoneNumber(phoneNumber);
    if (memberAuthorization == null) {
      throw new RestApiException(MemberAuthorizationErrorCode.CODE_NOT_SENT);
    }
    if (memberAuthorization.getCodeExpireTime().isBefore(LocalDateTime.now())) {
      throw new RestApiException(MemberAuthorizationErrorCode.CODE_EXPIRED);
    }
    if (!memberAuthorization.getAuthorizationCode().equals(authCode)) {
      throw new RestApiException(MemberAuthorizationErrorCode.CODE_INCORRECT);
    }
    Member member =
        memberRepository.findByPhoneNumber(phoneNumber).orElseGet(() -> memberRepository.save(new Member(phoneNumber)));
    String accessToken =
        jwtUtils.createJwt("access_token", member.getPhoneNumber(), member.getRole(), 2 * 24 * 60 * 60 * 1000L);

    return MemberLoginInfoResponse.of(member, accessToken);
  }

  /**
   * 핸드폰 번호를 받아서 문자를 보내고 해당 인증코드를 DB에 저장함
   *
   * @param phoneNumber
   */
  @Transactional
  public void loginWithPhoneNumber(String phoneNumber) {
    Integer authCode = phoneAuthorizationService.sendAuthMessage(phoneNumber);
    MemberAuthorization memberAuthorization = memberAuthorizationRepository.findByPhoneNumber(phoneNumber);
    if (memberAuthorization != null) {
      memberAuthorization.updateAuthorizationCode(authCode);
      return;
    }
    memberAuthorizationRepository.save(new MemberAuthorization(phoneNumber, authCode));
  }
}
