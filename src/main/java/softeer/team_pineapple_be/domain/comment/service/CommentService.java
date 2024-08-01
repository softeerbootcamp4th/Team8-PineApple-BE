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
    return CommentPageResponse.fromCommentPage(commentPage);
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
    return CommentPageResponse.fromCommentPage(commentPage);
  }

  /**
   * 기대평 작성 하는 메서드
   *
   * @param commentRequest
   */
  @Transactional
  public void saveComment(CommentRequest commentRequest) {
    String memberPhoneNumber = authMemberService.getMemberPhoneNumber();
    // 멤버 조회 로직이 다른 곳에 다 있어서 넣어뒀습니다.
    // 코드 보시고 불필요하다 생각 드시면 삭제하셔도 됩니다.
    // 아마 형님 로직은 phoneNumber 자체를 키로 둬서 안 쓰는 것 같은데 제꺼는 엔티티를 가져와야 해서 일단 넣어두겠습니다.
    memberRepository.findByPhoneNumber(memberPhoneNumber)
            .orElseThrow(()-> new RestApiException(MemberErrorCode.NO_MEMBER));
    if (wasMemberCommentedToday(memberPhoneNumber)) {
      throw new RestApiException(CommentErrorCode.ALREADY_REVIEWED);
    }
    commentRepository.save(new Comment(commentRequest.getContent(), memberPhoneNumber));
  }

  @Transactional
  public void saveCommentLike(CommentLikeRequest commentLikeRequest) {
    String memberPhoneNumber = authMemberService.getMemberPhoneNumber();
    // 멤버 조회 로직이 다른 곳에 다 있어서 넣어뒀습니다.
    // 코드 보시고 불필요하다 생각 드시면 삭제하셔도 됩니다.
    // 아마 형님 로직은 phoneNumber 자체를 키로 둬서 안 쓰는 것 같은데 제꺼는 엔티티를 가져와야 해서 일단 넣어두겠습니다.
    memberRepository.findByPhoneNumber(memberPhoneNumber)
            .orElseThrow(()-> new RestApiException(MemberErrorCode.NO_MEMBER));
    LikeId likeId = new LikeId(commentLikeRequest.getCommentId(), memberPhoneNumber);
    Optional<CommentLike> byId = commentLikeRepository.findById(likeId);
    if (byId.isPresent()) {
      // 예외처리 로직이 아닌 취소 처리가 되어야 할 것 같아 남겨두었습니다.
      //TODO: 이미 좋아요 누름 취소처리
      return;
    }
    Comment comment = commentRepository.findById(commentLikeRequest.getCommentId())
                                       .orElseThrow(() -> new RestApiException(CommentErrorCode.NO_COMMENT));
    comment.increaseLikeCount();
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
