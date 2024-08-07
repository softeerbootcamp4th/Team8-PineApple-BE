package softeer.team_pineapple_be.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import softeer.team_pineapple_be.domain.member.request.LoginAuthCodeRequest;
import softeer.team_pineapple_be.domain.member.request.LoginPhoneNumberRequest;
import softeer.team_pineapple_be.domain.member.response.MemberLoginInfoResponse;
import softeer.team_pineapple_be.domain.member.service.MemberAuthorizationService;
import softeer.team_pineapple_be.global.auth.annotation.Auth;
import softeer.team_pineapple_be.global.common.response.SuccessResponse;

/**
 * 로그인 컨트롤러
 */
@Tag(name = "인증 API", description = "인증 처리")
@RestController
@RequiredArgsConstructor
public class LoginController {
  public final MemberAuthorizationService memberAuthorizationService;


  @Operation(summary = "핸드폰 번호 입력해서 인증번호 받기")
  @PostMapping("/login/phone")
  public ResponseEntity<SuccessResponse> loginPhone(
      @Valid @RequestBody LoginPhoneNumberRequest loginPhoneNumberRequest) {
    memberAuthorizationService.loginWithPhoneNumber(loginPhoneNumberRequest.getPhoneNumber());
    return ResponseEntity.ok(new SuccessResponse());
  }

  @Operation(summary = "인증번호 입력해서 인증")
  @PostMapping("/login/code")
  public ResponseEntity<MemberLoginInfoResponse> loginWithAuthCode(
      @Valid @RequestBody LoginAuthCodeRequest loginAuthCodeRequest) {
    MemberLoginInfoResponse memberLoginInfoResponse =
        memberAuthorizationService.loginWithAuthCode(loginAuthCodeRequest.getPhoneNumber(),
            loginAuthCodeRequest.getCode());
    return ResponseEntity.ok(memberLoginInfoResponse);
  }

  @Auth
  @Operation(summary = "로그아웃")
  @PostMapping("/logout")
  public ResponseEntity<SuccessResponse> logout() {
    return ResponseEntity.ok(new SuccessResponse());
  }
}
