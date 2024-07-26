package softeer.team_pineapple_be.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.member.domain.Member;
import softeer.team_pineapple_be.domain.member.domain.MemberAuthorization;
import softeer.team_pineapple_be.domain.member.repository.MemberAuthorizationRepository;
import softeer.team_pineapple_be.domain.member.repository.MemberRepository;
import softeer.team_pineapple_be.domain.member.response.MemberInfoResponse;
import softeer.team_pineapple_be.global.auth.service.PhoneAuthorizationService;

/**
 * 멤버 로그인 인증번호 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberAuthorizationService {
  private final MemberRepository memberRepository;
  private final PhoneAuthorizationService phoneAuthorizationService;
  private final MemberAuthorizationRepository memberAuthorizationRepository;

  /**
   * 인증코드와 전화번호를 받아서 올바른 인증코드를 입력했는지 확인하고 멤버정보 보냄.
   *
   * @param phoneNumber
   * @param authCode
   * @return MemberInfoResponse
   */
  @Transactional
  public MemberInfoResponse loginWithAuthCode(String phoneNumber, Integer authCode) {
    MemberAuthorization memberAuthorization = memberAuthorizationRepository.findByPhoneNumber(phoneNumber);
    if (memberAuthorization == null) {
      //TODO: 인증번호 보낸 적 없음 예외처리
      throw new RuntimeException();
    }
    if (memberAuthorization.getCodeExpireTime().isBefore(LocalDateTime.now())) {
      //TODO: 인증 시간 지남 예외처리
      throw new RuntimeException("");
    }
    if (!memberAuthorization.getAuthorizationCode().equals(authCode)) {
      //TODO: 인증번호 틀림 예외처리
      throw new RuntimeException();
    }
    Member member = memberRepository.findByPhoneNumber(phoneNumber);
    if (member == null) {
      member = memberRepository.save(new Member(phoneNumber));
    }
    return MemberInfoResponse.of(member);
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
