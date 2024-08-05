package softeer.team_pineapple_be.global.auth.context;

/**
 * 인증 컨텍스트 홀더
 */
public class AuthContextHolder {
  private static final ThreadLocal<AuthContext> contextHolder = new ThreadLocal<>();

  public static void clearContext() {
    contextHolder.remove();
  }

  public static AuthContext getAuthContext() {
    return contextHolder.get();
  }

  public static void setAuthContext(AuthContext authContext) {
    contextHolder.set(authContext);
  }
}
