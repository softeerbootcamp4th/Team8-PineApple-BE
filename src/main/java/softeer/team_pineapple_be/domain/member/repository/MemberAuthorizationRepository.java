package softeer.team_pineapple_be.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softeer.team_pineapple_be.domain.member.domain.MemberAuthorization;

/**
 * 멤버 로그인 인증번호 리포지토리
 */
public interface MemberAuthorizationRepository extends JpaRepository<MemberAuthorization, Long> {
  public MemberAuthorization findByPhoneNumber(String phoneNumber);
}
