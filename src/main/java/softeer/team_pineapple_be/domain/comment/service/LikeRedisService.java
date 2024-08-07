package softeer.team_pineapple_be.domain.comment.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.List;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.comment.domain.CommentLike;
import softeer.team_pineapple_be.domain.comment.domain.id.LikeId;
import softeer.team_pineapple_be.domain.comment.repository.CommentLikeRepository;
import softeer.team_pineapple_be.global.auth.context.AuthContext;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;

/**
 * 레디스를 이용한 좋아요 서비스
 */
@Service
@RequiredArgsConstructor
public class LikeRedisService {
  private static final String LIKE_PREFIX = "comment:";
  private final RedisTemplate<String, String> redisTemplate;
  private final AuthMemberService authMemberService;
  private final CommentLikeRepository commentLikeRepository;

  /**
   * 좋아요 정보 레디스에 추가
   *
   * @param commentId
   */
  public void addLike(Long commentId) {
    String memberPhoneNumber = authMemberService.getMemberPhoneNumber();
    SetOperations<String, String> setOps = redisTemplate.opsForSet();
    setOps.add(LIKE_PREFIX + commentId, memberPhoneNumber);
  }

  /**
   * 빈 초기화 할 때 좋아요 정보 DB에서 꺼내서 올려놓기
   */
  @PostConstruct
  public void initializeRedisStorage() {
    List<CommentLike> allLikes = commentLikeRepository.findAll();
    SetOperations<String, String> setOps = redisTemplate.opsForSet();
    for (CommentLike commentLike : allLikes) {
      LikeId commentIdAndPhoneNumber = commentLike.getId();
      Long commentId = commentIdAndPhoneNumber.getCommentId();
      String phoneNumber = commentIdAndPhoneNumber.getPhoneNumber();
      setOps.add(LIKE_PREFIX + commentId, phoneNumber);
    }
  }

  /**
   * 자신이 좋아요 눌렀는지 확인
   *
   * @param commentId
   * @return 좋아요 누름 여부
   */
  public Boolean isLiked(Long commentId) {
    SetOperations<String, String> setOps = redisTemplate.opsForSet();
    AuthContext authContext = authMemberService.getAuthContext();
    if (authContext == null) {
      return false;
    }
    String memberPhoneNumber = authContext.getPhoneNumber();
    return setOps.isMember(LIKE_PREFIX + commentId, memberPhoneNumber);
  }

  /**
   * 좋아요 취소
   *
   * @param commentId
   */
  public void removeLike(Long commentId) {
    String memberPhoneNumber = authMemberService.getMemberPhoneNumber();
    SetOperations<String, String> setOps = redisTemplate.opsForSet();
    setOps.remove(LIKE_PREFIX + commentId, memberPhoneNumber);
  }
}
