package softeer.team_pineapple_be.domain.member.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softeer.team_pineapple_be.domain.member.domain.Member;

/**
 * 로그인했을 때 주는 응답
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberLoginInfoResponse {
  private String phoneNumber;
  private Integer toolBoxCnt;
  private boolean car;
  private String accessToken;

  /**
   * 로그인한 유저에게 응답 주기
   *
   * @param member
   * @return MemberInfoResponse
   */
  public static MemberLoginInfoResponse of(Member member, String accessToken) {
    return new MemberLoginInfoResponse(member.getPhoneNumber(), member.getToolBoxCnt(), member.isCar(), accessToken);
  }
}
