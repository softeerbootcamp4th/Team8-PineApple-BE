package softeer.team_pineapple_be.domain.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.comment.request.CommentRequest;
import softeer.team_pineapple_be.domain.comment.response.CommentPageResponse;
import softeer.team_pineapple_be.domain.comment.service.CommentService;
import softeer.team_pineapple_be.global.common.response.SuccessResponse;

/**
 * 기대평 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
  private final CommentService commentService;

  @PostMapping
  public ResponseEntity<SuccessResponse> addComment(@RequestBody CommentRequest commentRequest) {
    commentService.saveComment(commentRequest);
    return ResponseEntity.ok(new SuccessResponse());
  }

  @GetMapping
  public ResponseEntity<CommentPageResponse> getComments(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "") String sort) {
    if (sort.equals("like")) {
      return ResponseEntity.ok(commentService.getCommentsSortedByLikes(page));
    } else {
      return ResponseEntity.ok(commentService.getCommentsSortedByRecent(page));
    }
  }
}
