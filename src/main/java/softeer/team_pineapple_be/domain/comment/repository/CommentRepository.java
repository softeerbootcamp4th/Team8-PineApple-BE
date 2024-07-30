package softeer.team_pineapple_be.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

import softeer.team_pineapple_be.domain.comment.domain.Comment;

/**
 * 기대평 리포지토리
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
  Page<Comment> findAllByPostTimeBetween(Pageable pageable, LocalDateTime startOfDay, LocalDateTime endOfDay);

  @Query(
      "SELECT c FROM Comment c WHERE c.phoneNumber = :phoneNumber AND c.postTime >= :startOfDay AND c.postTime < :endOfDay")
  Optional<Comment> findCommentsByAuthorAndDate(@Param("phoneNumber") String phoneNumber,
      @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
