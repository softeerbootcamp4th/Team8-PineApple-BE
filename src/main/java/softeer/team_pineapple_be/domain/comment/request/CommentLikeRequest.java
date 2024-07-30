package softeer.team_pineapple_be.domain.comment.request;

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
  private Long commentId;
}
