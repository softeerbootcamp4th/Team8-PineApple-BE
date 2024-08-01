package softeer.team_pineapple_be.domain.comment.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 기대평 작성 요청 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
  @NotNull(message = "{comment.content}")
  @Length(min = 1, max = 50, message = "{comment.content_length_range}")
  private String content;
}
