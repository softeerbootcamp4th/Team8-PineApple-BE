package softeer.team_pineapple_be.domain.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.comment.domain.Comment;
import softeer.team_pineapple_be.domain.comment.repository.CommentLikeRepository;
import softeer.team_pineapple_be.domain.comment.repository.CommentRepository;
import softeer.team_pineapple_be.domain.comment.response.CommentPageResponse;

/**
 * 기대평 서비스
 */
@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final CommentLikeRepository commentLikeRepository;

  //TODO : 좋아요 기능 구현
  public CommentPageResponse getCommentsSortedByLikes(int page) {
    Page<Comment> commentPage = commentRepository.findAll(PageRequest.of(page, 10));
    return CommentPageResponse.fromCommentPage(commentPage);
  }

  public CommentPageResponse getCommentsSortedByRecent(int page) {
    PageRequest commentRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "postTime"));
    Page<Comment> commentPage = commentRepository.findAll(commentRequest);
    return CommentPageResponse.fromCommentPage(commentPage);
  }
}
