package softeer.team_pineapple_be.domain.member.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import softeer.team_pineapple_be.domain.member.domain.Member;

/**
 * 멤버 정보 응답
 */
@AllArgsConstructor
@Getter
public class MemberInfoResponse {
  private String phoneNumber;
  private Integer toolBoxCnt;
  private boolean car;

  /**
   * 멤버 엔티티를 멤버 정보 응답 Response 로 변환
   *
   * @param member
   * @return MemberInfoResponse
   */
  public static MemberInfoResponse of(Member member) {
    return new MemberInfoResponse(member.getPhoneNumber(), member.getToolBoxCnt(), member.isCar());
  }
}
