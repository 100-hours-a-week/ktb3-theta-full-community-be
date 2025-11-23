package ktb.week4.community.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ktb.week4.community.domain.user.dto.LoginResponseDto;
import ktb.week4.community.global.apiPayload.ApiResponse;
import ktb.week4.community.global.apiPayload.SuccessCode;
import ktb.week4.community.security.CustomUserDetails;
import ktb.week4.community.security.JwtTokenProvider;
import ktb.week4.community.security.filter.JsonAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final AuthenticationConfiguration authenticationConfiguration;
	private final ObjectMapper objectMapper;
	private final JwtTokenProvider jwtTokenProvider;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				// 임시 처리
				
				.formLogin(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/auth/login/**",  "/users/**").anonymous()
						.requestMatchers(HttpMethod.GET, "/articles/**").permitAll()
						.anyRequest().authenticated()
				)
				.addFilterAt(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	private JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
		JsonAuthenticationFilter filter = new JsonAuthenticationFilter(objectMapper);
		
		// AuthenticationManager
		filter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
		
		// SuccessHandler
		filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
			response.setStatus(200);
			response.setContentType("application/json;charset=UTF-8");
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			
			ResponseCookie accessToken = ResponseCookie.from("accessToken", jwtTokenProvider.createToken(authentication, 60))
					.path("/")
					.maxAge(60 * 60)
					.httpOnly(true)
					.build();
			response.addHeader(HttpHeaders.SET_COOKIE, accessToken.toString());
			
			ResponseCookie refreshToken = ResponseCookie.from("refreshToken", jwtTokenProvider.createToken(authentication, 7 * 24 * 60))
					.path("/auth/")
					.maxAge(7 * 24 * 60 * 60)
					.httpOnly(true)
					.build();
			response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());
			
			response.getWriter().write(objectMapper.writeValueAsString(
					ApiResponse.onSuccess(SuccessCode.SUCCESS, new LoginResponseDto(
							userDetails.getUserId(), userDetails.getUserProfileImage())
					))
			);
		});
		
		// FailureHandler
		filter.setAuthenticationFailureHandler((request,  response, exception) -> {
			response.setStatus(401);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(
					ApiResponse.onFailure("401",  "로그인에 실패하였습니다.")
			));
		});
		
		return filter;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
