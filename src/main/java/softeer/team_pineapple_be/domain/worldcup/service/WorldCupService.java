package softeer.team_pineapple_be.domain.worldcup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.member.domain.Member;
import softeer.team_pineapple_be.domain.member.exception.MemberErrorCode;
import softeer.team_pineapple_be.domain.member.repository.MemberRepository;
import softeer.team_pineapple_be.domain.worldcup.response.WorldCupParticipateResponse;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;
import softeer.team_pineapple_be.global.exception.RestApiException;

/**
 * 월드컵 서비스
 */
@Service
@RequiredArgsConstructor
public class WorldCupService {
  private final MemberRepository memberRepository;
  private final AuthMemberService authMemberService;

  /**
   * 멤버가 월드컵에 참여했는지 여부를 응답으로 반환하는 메서드
   *
   * @return 월드컵 참여여부 응답
   */
  public WorldCupParticipateResponse isMemberParticipated() {
    String memberPhoneNumber = authMemberService.getMemberPhoneNumber();
    Member member = memberRepository.findByPhoneNumber(memberPhoneNumber)
            .orElseThrow(()-> new RestApiException(MemberErrorCode.NO_MEMBER));
    return new WorldCupParticipateResponse(member.isCar());
  }

  /**
   * 월드컵에 참여하는 메서드
   */
  @Transactional
  public void participateWorldCup() {
    String phoneNumber = authMemberService.getMemberPhoneNumber();
    Member member = memberRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(()-> new RestApiException(MemberErrorCode.NO_MEMBER));
    member.generateCar();
  }
}
