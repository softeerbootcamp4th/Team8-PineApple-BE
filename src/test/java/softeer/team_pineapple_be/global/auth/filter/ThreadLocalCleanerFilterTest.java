package softeer.team_pineapple_be.global.auth.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import softeer.team_pineapple_be.global.auth.context.AuthContext;
import softeer.team_pineapple_be.global.auth.context.AuthContextHolder;

public class ThreadLocalCleanerFilterTest {

    private ThreadLocalCleanerFilter filter;
    private AuthContext authContext;
    private ServletRequest request;
    private ServletResponse response;
    private FilterChain chain;

    @BeforeEach
    public void setUp() {
        filter = new ThreadLocalCleanerFilter();
        authContext = new AuthContext("010-1234-5678", "USER");
        request = mock(ServletRequest.class);
        response = mock(ServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    @DisplayName("필터에서 ThreadLocal 클린이 성공적으로 동작한 경우 - SuccessCase")
    public void doFilter_clearsThreadLocal() throws IOException, ServletException {
        // When
        AuthContextHolder.setAuthContext(authContext);
        filter.doFilter(request, response, chain);

        // Then
        verify(chain).doFilter(request, response);
        assertThat(AuthContextHolder.getAuthContext()).isNull();
    }

    @Test
    @DisplayName("필터에서 ThreadLocal 예외처리가 성공적으로 동작한 경우 - SuccessCase")
    public void doFilter_exceptionClearsThreadLocal() throws IOException, ServletException {
        // When
        AuthContextHolder.setAuthContext(authContext);
        doThrow(new ServletException("Test Exception")).when(chain).doFilter(request, response);
        try {
            filter.doFilter(request, response, chain);
        } catch (ServletException e) {
        }

        // Then
        assertThat(AuthContextHolder.getAuthContext()).isNull();
    }

    @Test
    @DisplayName("시작 시 초기화에서 예외가 발생하지 않는 경우 - SuccessCase")
    public void init_NotThrowException() throws ServletException {
        // Given
        FilterConfig filterConfig = mock(FilterConfig.class);

        // When
        filter.init(filterConfig);

        // Then
        // 예외가 발생하지 않으면 테스트 통과
    }

    @Test
    @DisplayName("Filter destroy에서 예외가 발생하지 않는 경우 - SuccessCase")
    public void destroy_NotThrowException() {
        // When
        filter.destroy();

        //Then
        // 예외가 발생하지 않으면 테스트 통과
    }
}
