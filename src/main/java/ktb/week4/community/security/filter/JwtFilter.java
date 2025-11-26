package ktb.week4.community.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ktb.week4.community.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		Cookie[] cookies = request.getCookies();
		try {
			if (cookies != null && Arrays.stream(cookies).anyMatch(cookie -> cookie.getName().equals("accessToken"))) {
				String accessToken = Arrays.stream(cookies)
						.filter(cookie -> cookie.getName().equals("accessToken"))
						.map(Cookie::getValue)
						.findFirst()
						.orElse(null);
				
				jwtTokenProvider.validateToken(accessToken);
				Authentication auth = jwtTokenProvider.resolveToken(accessToken);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
		}
		
		filterChain.doFilter(request, response);
	}
}
