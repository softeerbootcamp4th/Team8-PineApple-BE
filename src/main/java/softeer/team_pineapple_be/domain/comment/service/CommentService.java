package softeer.team_pineapple_be.domain.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import softeer.team_pineapple_be.domain.comment.domain.Comment;
import softeer.team_pineapple_be.domain.comment.repository.CommentLikeRepository;
import softeer.team_pineapple_be.domain.comment.repository.CommentRepository;
import softeer.team_pineapple_be.domain.comment.request.CommentRequest;
import softeer.team_pineapple_be.domain.comment.response.CommentPageResponse;
import softeer.team_pineapple_be.domain.member.repository.MemberRepository;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;

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
  public CommentPageResponse getCommentsSortedByLikes(int page) {
    Page<Comment> commentPage =
        commentRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "likeCount")));
    return CommentPageResponse.fromCommentPage(commentPage);
  }

  /**
   * 기대평을 최신순으로 가져오는 메서드
   *
   * @param page
   * @return 최신순 정렬 기대평 목록
   */
  public CommentPageResponse getCommentsSortedByRecent(int page) {
    PageRequest commentRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "postTime"));
    Page<Comment> commentPage = commentRepository.findAll(commentRequest);
    return CommentPageResponse.fromCommentPage(commentPage);
  }

  /**
   * 기대평 작성 하는 메서드
   *
   * @param commentRequest
   */
  public void saveComment(CommentRequest commentRequest) {
    String memberPhoneNumber = authMemberService.getMemberPhoneNumber();
    if (wasMemberCommentedToday(memberPhoneNumber)) {
      log.info("이미 기대평 작성");
      return;
    }
    commentRepository.save(new Comment(commentRequest.getContent(), memberPhoneNumber));
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
