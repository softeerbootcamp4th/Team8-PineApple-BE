package softeer.team_pineapple_be.domain.comment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.comment.response.CommentPageResponse;
import softeer.team_pineapple_be.domain.comment.service.CommentService;

/**
 * 기대평 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
  private final CommentService commentService;

  @GetMapping
  public CommentPageResponse getComments(@RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "") String sort) {
    System.out.println(sort);
    if (sort.equals("like")) {
      return commentService.getCommentsSortedByLikes(page);
    } else {
      return commentService.getCommentsSortedByRecent(page);
    }
  }
}
