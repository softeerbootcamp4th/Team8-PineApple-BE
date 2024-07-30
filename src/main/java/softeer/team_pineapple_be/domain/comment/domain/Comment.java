package softeer.team_pineapple_be.domain.comment.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 엔티티
 */
@Entity
@NoArgsConstructor
@Getter
public class Comment {
  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String phoneNumber;
  @Column(nullable = false)
  private String content;
  @Column(nullable = false)
  private Integer likeCount;
  @Column(nullable = false)
  private LocalDateTime postTime;
}
