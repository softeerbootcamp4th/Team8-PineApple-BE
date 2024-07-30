package softeer.team_pineapple_be.domain.comment.response;

import org.springframework.data.domain.Page;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import softeer.team_pineapple_be.domain.comment.domain.Comment;

/**
 * 기대평 목록 응답
 */
@Getter
@Setter
@AllArgsConstructor
public class CommentPageResponse {
  private Integer totalPages;
  private List<CommentResponse> comments;

  public static CommentPageResponse fromCommentPage(Page<Comment> comments) {
    List<Comment> content = comments.getContent();
    List<CommentResponse> commentResponseList = content.stream().map(CommentResponse::fromComment).toList();
    return new CommentPageResponse(comments.getTotalPages(), commentResponseList);
  }
}
