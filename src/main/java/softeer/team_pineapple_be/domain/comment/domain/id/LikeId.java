package softeer.team_pineapple_be.domain.comment.domain.id;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 좋아요 복합키
 */
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LikeId implements Serializable {
  @Column(nullable = false)
  private Long commentId;
  @Column(nullable = false)
  private String phoneNumber;
}
