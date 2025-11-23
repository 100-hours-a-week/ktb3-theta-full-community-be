package ktb.week4.community.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
	private final String secret;
	
	public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
		this.secret = secret;
	}
	
	public String createToken(Authentication authentication, long validMinutes) {
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		
		long now = (new Date()).getTime();
		Date expiresAt = new Date(now + validMinutes * 60 * 1000);
		
		return Jwts.builder()
				.subject(authentication.getName())
				.claim("authorities", authorities)
				.claim("userId", userDetails.getUserId())
				.claim("userName", userDetails.getUsername())
				.claim("profileImage", userDetails.getUserProfileImage())
				.signWith(getSecretKey())
				.expiration(expiresAt)
				.compact();
	}
	
	public Authentication resolveToken(String token) {
		Claims claims = Jwts.parser()
				.decryptWith(getSecretKey())
				.build()
				.parseUnsecuredClaims(token)
				.getPayload();
		
		Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("authorities").toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.toList();
		Long id =  Long.parseLong(claims.get("userId").toString());
		String userName = claims.get("userName").toString();
		String userProfileImage = claims.get("profileImage").toString();
		CustomUserDetails principal = new CustomUserDetails(
				userName, "", authorities, id, userProfileImage
		);
		
		return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
	}
	
	private SecretKey getSecretKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
