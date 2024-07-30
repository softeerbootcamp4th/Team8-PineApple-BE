package softeer.team_pineapple_be.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softeer.team_pineapple_be.domain.comment.domain.Comment;

/**
 * 기대평 리포지토리
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
