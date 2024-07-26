package softeer.team_pineapple_be.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.member.request.LoginAuthCodeRequest;
import softeer.team_pineapple_be.domain.member.request.LoginPhoneNumberRequest;
import softeer.team_pineapple_be.domain.member.response.MemberInfoResponse;
import softeer.team_pineapple_be.domain.member.service.MemberAuthorizationService;
import softeer.team_pineapple_be.global.common.response.SuccessResponse;

/**
 * 로그인 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class LoginController {
  public final MemberAuthorizationService memberAuthorizationService;

  
  @Tag(name = "인증 API", description = "인증 처리")
  @PostMapping("/login/phone")
  public ResponseEntity<SuccessResponse> loginPhone(@RequestBody LoginPhoneNumberRequest loginPhoneNumberRequest) {
    memberAuthorizationService.loginWithPhoneNumber(loginPhoneNumberRequest.getPhoneNumber());
    return ResponseEntity.ok(new SuccessResponse());
  }

  @Tag(name = "인증 API")
  @PostMapping("/login/code")
  public ResponseEntity<MemberInfoResponse> loginWithAuthCode(@RequestBody LoginAuthCodeRequest loginAuthCodeRequest,
      HttpServletRequest request) {
    MemberInfoResponse memberInfoResponse =
        memberAuthorizationService.loginWithAuthCode(loginAuthCodeRequest.getPhoneNumber(),
            loginAuthCodeRequest.getCode());
    HttpSession session = request.getSession();
    session.setAttribute("phoneNumber", memberInfoResponse.getPhoneNumber());
    return ResponseEntity.ok(memberInfoResponse);
  }

  @Tag(name = "인증 API")
  @PostMapping("/logout")
  public ResponseEntity<SuccessResponse> logout(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    return ResponseEntity.ok(new SuccessResponse());
  }
}
