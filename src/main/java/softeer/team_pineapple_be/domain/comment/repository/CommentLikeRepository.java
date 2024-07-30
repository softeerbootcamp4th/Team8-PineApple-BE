package softeer.team_pineapple_be.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softeer.team_pineapple_be.domain.comment.domain.CommentLike;
import softeer.team_pineapple_be.domain.comment.domain.id.LikeId;

/**
 * 좋아요 리포지토리
 */
public interface CommentLikeRepository extends JpaRepository<CommentLike, LikeId> {
}
