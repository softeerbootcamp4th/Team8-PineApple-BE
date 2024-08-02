package softeer.team_pineapple_be.domain.comment.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 좋아요 요청
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentLikeRequest {
  @NotNull(message = "{comment.comment_id_required}")
  @Min(value = 1, message = "{comment.comment_id_min}")
  private Long commentId;
}
