package softeer.team_pineapple_be.domain.draw.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.draw.domain.DrawDailyMessageInfo;
import softeer.team_pineapple_be.domain.draw.domain.DrawHistory;
import softeer.team_pineapple_be.domain.draw.domain.DrawPrize;
import softeer.team_pineapple_be.domain.draw.domain.DrawRewardInfo;
import softeer.team_pineapple_be.domain.draw.repository.DrawDailyMessageInfoRepository;
import softeer.team_pineapple_be.domain.draw.repository.DrawHistoryRepository;
import softeer.team_pineapple_be.domain.draw.repository.DrawPrizeRepository;
import softeer.team_pineapple_be.domain.draw.repository.DrawRewardInfoRepository;
import softeer.team_pineapple_be.domain.draw.response.DrawLoseResponse;
import softeer.team_pineapple_be.domain.draw.response.DrawResponse;
import softeer.team_pineapple_be.domain.draw.response.DrawWinningResponse;
import softeer.team_pineapple_be.domain.member.domain.Member;
import softeer.team_pineapple_be.domain.member.repository.MemberRepository;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;

/**
 * 경품 추첨 서비스
 */
@Service
@RequiredArgsConstructor
public class DrawService {
  private final DrawDailyMessageInfoRepository drawDailyMessageInfoRepository;
  private final DrawHistoryRepository drawHistoryRepository;
  private final DrawPrizeRepository drawPrizeRepository;
  private final DrawRewardInfoRepository drawRewardInfoRepository;
  private final AuthMemberService authMemberService;
  private final MemberRepository memberRepository;
  private final RandomDrawPrizeService randomDrawPrizeService;

  /**
   * 경품 추첨 수행하는 메서드
   *
   * @return 경품에 대한 정보 응답 객체
   */
  @Transactional
  public DrawResponse enterDraw() {
    String memberPhoneNumber = authMemberService.getMemberPhoneNumber();
    Member member =
        memberRepository.findById(memberPhoneNumber).orElseThrow(() -> new RuntimeException("member not found"));
    canEnterDraw(member);
    member.decrementToolBoxCnt();
    Byte prizeRank = randomDrawPrizeService.drawPrize();
    DrawRewardInfo rewardInfo =
        drawRewardInfoRepository.findById(prizeRank).orElseThrow(() -> new RuntimeException("prize not found"));
    DrawDailyMessageInfo dailyMessageInfo = drawDailyMessageInfoRepository.findByDrawDate(LocalDate.now());
    if (rewardInfo.getStock() == 0 || rewardInfo.getRanking() == 0) {
      drawHistoryRepository.save(new DrawHistory((byte) 0, memberPhoneNumber));
      return new DrawLoseResponse(dailyMessageInfo.getLoseMessage(), dailyMessageInfo.getLoseScenario(),
          dailyMessageInfo.getLoseImage(), member.isCar(), member.getToolBoxCnt());
    }
    drawHistoryRepository.save(new DrawHistory(prizeRank, memberPhoneNumber));
    rewardInfo.decreaseStock();
    Long prizeId = setPrizeOwner(rewardInfo, memberPhoneNumber);
    return new DrawWinningResponse(dailyMessageInfo.getWinMessage(), rewardInfo.getName(),
        dailyMessageInfo.getWinImage(), prizeId, member.isCar(), member.getToolBoxCnt());
  }

  /**
   * 경품 추첨 자격 있는지 확인
   *
   * @param member
   */
  private void canEnterDraw(Member member) {
    if ((!member.isCar()) || (member.getToolBoxCnt() == 0)) {
      throw new RuntimeException("can't enter draw");
    }
  }

  /**
   * 당첨된 경품에 소유자 설정
   *
   * @param rewardInfo
   * @param memberPhoneNumber
   * @return 경품 ID
   */
  private Long setPrizeOwner(DrawRewardInfo rewardInfo, String memberPhoneNumber) {
    DrawPrize prize = drawPrizeRepository.findFirstByDrawRewardInfoAndValid(rewardInfo, true)
                                         .orElseThrow(() -> new RuntimeException("재고에는 있다고 뜨는데 해당하는 경품이 없음"));
    prize.isNowOwnedBy(memberPhoneNumber);
    prize.invalidate();
    return prize.getId();
  }
}
