package softeer.team_pineapple_be.domain.member.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 멤버 로그인 인증번호 엔티티
 */
@Entity
@NoArgsConstructor
@Getter
public class MemberAuthorization {
  @Id
  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private Integer authorizationCode;

  @Column(nullable = false)
  private LocalDateTime codeExpireTime;

  /**
   * 새 인증정보 생성
   *
   * @param phoneNumber
   * @param authorizationCode
   */
  public MemberAuthorization(String phoneNumber, Integer authorizationCode) {
    this.phoneNumber = phoneNumber;
    this.authorizationCode = authorizationCode;
    this.codeExpireTime = LocalDateTime.now().plusMinutes(3);
  }

  /**
   * 인증 코드 업데이트
   *
   * @param authorizationCode
   */
  public void updateAuthorizationCode(Integer authorizationCode) {
    this.authorizationCode = authorizationCode;
    this.codeExpireTime = LocalDateTime.now().plusMinutes(3);
  }
}

