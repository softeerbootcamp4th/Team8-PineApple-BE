package softeer.team_pineapple_be.domain.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import softeer.team_pineapple_be.domain.comment.domain.Comment;
import softeer.team_pineapple_be.domain.comment.domain.CommentLike;
import softeer.team_pineapple_be.domain.comment.domain.id.LikeId;
import softeer.team_pineapple_be.domain.comment.exception.CommentErrorCode;
import softeer.team_pineapple_be.domain.comment.repository.CommentLikeRepository;
import softeer.team_pineapple_be.domain.comment.repository.CommentRepository;
import softeer.team_pineapple_be.domain.comment.request.CommentLikeRequest;
import softeer.team_pineapple_be.domain.comment.request.CommentRequest;
import softeer.team_pineapple_be.domain.comment.response.CommentPageResponse;
import softeer.team_pineapple_be.domain.member.domain.Member;
import softeer.team_pineapple_be.domain.member.exception.MemberErrorCode;
import softeer.team_pineapple_be.domain.member.repository.MemberRepository;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;
import softeer.team_pineapple_be.global.exception.RestApiException;

/**
 * 기대평 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final CommentLikeRepository commentLikeRepository;
  private final AuthMemberService authMemberService;
  private final MemberRepository memberRepository;
  private final LikeRedisService likeRedisService;

  /**
   * 기대평을 좋아요 순으로 가져오는 메서드
   *
   * @param page
   * @return 좋아요 순 정렬 기대평 목록
   */
  public CommentPageResponse getCommentsSortedByLikes(int page, LocalDate date) {
    PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "likeCount"));
    Page<Comment> commentPage =
        commentRepository.findAllByPostTimeBetween(pageRequest, date.atStartOfDay(), date.atTime(LocalTime.MAX));
    return CommentPageResponse.fromCommentPage(commentPage, likeRedisService);
  }

  /**
   * 기대평을 최신순으로 가져오는 메서드
   *
   * @param page
   * @return 최신순 정렬 기대평 목록
   */
  public CommentPageResponse getCommentsSortedByRecent(int page, LocalDate date) {
    PageRequest commentRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "postTime"));
    Page<Comment> commentPage =
        commentRepository.findAllByPostTimeBetween(commentRequest, date.atStartOfDay(), date.atTime(LocalTime.MAX));
    return CommentPageResponse.fromCommentPage(commentPage, likeRedisService);
  }

  /**
   * 기대평 작성 하는 메서드
   *
   * @param commentRequest
   */
  @Transactional
  public void saveComment(CommentRequest commentRequest) {
    String memberPhoneNumber = authMemberService.getMemberPhoneNumber();
    if (wasMemberCommentedToday(memberPhoneNumber)) {
      throw new RestApiException(CommentErrorCode.ALREADY_REVIEWED);
    }
    Member member =
        memberRepository.findById(memberPhoneNumber).orElseThrow(() -> new RestApiException(MemberErrorCode.NO_MEMBER));
    member.incrementToolBoxCnt();
    commentRepository.save(new Comment(commentRequest.getContent(), memberPhoneNumber));
  }

  /**
   * 좋아요 누름 처리하는 메서드 이미 눌렀으면 좋아요를 줄이고, 안눌렀으면 좋아요 증가시킴
   * TODO: 동시성 문제가 발생할 수 있으니 처리하자
   *
   * @param commentLikeRequest
   */
  @Transactional
  public void saveCommentLike(CommentLikeRequest commentLikeRequest) {
    String memberPhoneNumber = authMemberService.getMemberPhoneNumber();
    LikeId likeId = new LikeId(commentLikeRequest.getCommentId(), memberPhoneNumber);
    Optional<CommentLike> byId = commentLikeRepository.findById(likeId);
    Comment comment = commentRepository.findById(commentLikeRequest.getCommentId())
                                       .orElseThrow(() -> new RestApiException(CommentErrorCode.NO_COMMENT));
    if (byId.isPresent()) {
      likeRedisService.removeLike(comment.getId());
      comment.decreaseLikeCount();
      commentLikeRepository.delete(byId.get());
      return;
    }
    comment.increaseLikeCount();
    likeRedisService.addLike(comment.getId());
    commentLikeRepository.save(new CommentLike(likeId));
  }

  /**
   * 오늘 이미 기대평 작성했는지 확인하는 메서드
   *
   * @param memberPhoneNumber
   * @return 오늘 기대평 작성 여부
   */
  private boolean wasMemberCommentedToday(String memberPhoneNumber) {
    LocalDate today = LocalDate.now();
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
    Optional<Comment> commentsByAuthorAndDate =
        commentRepository.findCommentsByAuthorAndDate(memberPhoneNumber, startOfDay, endOfDay);
    return commentsByAuthorAndDate.isPresent();
  }
}
