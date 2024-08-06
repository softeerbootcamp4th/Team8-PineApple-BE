package softeer.team_pineapple_be.domain.comment.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import softeer.team_pineapple_be.domain.comment.domain.Comment;
import softeer.team_pineapple_be.domain.comment.utils.PhoneNumberMasker;

/**
 * 기대평 응답
 */
@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
  private Long id;
  private String phoneNumber;
  private String content;
  private Integer likeCount;
  private LocalDateTime postTime;

  public static CommentResponse fromComment(Comment comment) {
    return new CommentResponse(comment.getId(), PhoneNumberMasker.maskPhoneNumber(comment.getPhoneNumber()),
        comment.getContent(), comment.getLikeCount(), comment.getPostTime());
  }
}
