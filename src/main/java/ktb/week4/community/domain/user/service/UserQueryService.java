package ktb.week4.community.domain.user.service;

import ktb.week4.community.domain.user.dto.LoginRequestDto;
import ktb.week4.community.domain.user.dto.LoginResponseDto;
import ktb.week4.community.domain.user.dto.UserResponseDto;
import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.domain.user.policy.UserPolicy;
import ktb.week4.community.domain.user.loader.UserLoader;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {
	private final UserLoader userLoader;
	private final UserPolicy userPolicy;
	
	public UserResponseDto getUser(Long userId) {
		User user = userLoader.getUserById(userId);
		return UserResponseDto.fromEntity(user);
	}
	
	public LoginResponseDto login(LoginRequestDto request) {
		User user = userLoader.getUserByEmail(request.email());
		
		userPolicy.matchPassword(user, request.password());
		
		// 토큰 발급 등 로그인 처리 로직 진행
		return new LoginResponseDto(user.getId(), user.getProfileImage());
	}
	
	public void logout(Long userId) {
		User user = userLoader.getUserById(userId);
		// 토큰 블랙리스트 처리, 제거 등 로그아웃 처리 로직 진행
	}
}
