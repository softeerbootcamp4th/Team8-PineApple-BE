package softeer.team_pineapple_be.global.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;
import softeer.team_pineapple_be.global.auth.exception.AuthErrorCode;
import softeer.team_pineapple_be.global.exception.RestApiException;

/**
 * JWT 유틸 클래스
 */
@Component
public class JwtUtils {
  private final SecretKey secretKey;

  public JwtUtils(@Value("${spring.jwt.secret}") String secret) {
    secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public String createJwt(String category, String phoneNumber, String role, Long expiredMs) {
    return Jwts.builder()
               .claim("category", category)
               .claim("phoneNumber", phoneNumber)
               .claim("role", role)
               .issuedAt(new Date(System.currentTimeMillis()))
               .expiration(new Date(System.currentTimeMillis() + expiredMs))
               .signWith(secretKey)
               .compact();
  }

  public String getCategory(String token) {
    String category;
    try {
      category = Jwts.parser()
                     .verifyWith(secretKey)
                     .build()
                     .parseSignedClaims(token)
                     .getPayload()
                     .get("category", String.class);
    } catch (Exception e) {
      throw new RestApiException(AuthErrorCode.JWT_PARSING_ERROR);
    }
    return category;
  }

  public String getPhoneNumber(String token) {
    String phoneNumber;
    try {
      phoneNumber = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .get("phoneNumber", String.class);
    } catch (Exception e) {
      throw new RestApiException(AuthErrorCode.JWT_PARSING_ERROR);
    }
    return phoneNumber;
  }

  public String getRole(String token) {
    String role;
    try {
      role =
          Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    } catch (Exception e) {
      throw new RestApiException(AuthErrorCode.JWT_PARSING_ERROR);
    }
    return role;
  }

  public Boolean isExpired(String token) {
    boolean before;
    try {
      before = Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .getExpiration()
                   .before(new Date());
    } catch (Exception e) {
      throw new RestApiException(AuthErrorCode.JWT_EXPIRED);
    }
    return before;
  }
}
