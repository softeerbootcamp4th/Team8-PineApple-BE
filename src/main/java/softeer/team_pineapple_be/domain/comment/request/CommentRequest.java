package softeer.team_pineapple_be.domain.comment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 기대평 작성 요청 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
  private String content;
}
