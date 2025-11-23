package ktb.week4.community.domain.user.service;

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
	
	public UserResponseDto getUser(Long userId) {
		User user = userLoader.getUserById(userId);
		return UserResponseDto.fromEntity(user);
	}
	
	public void logout(Long userId) {
		// 토큰 블랙리스트 처리, 제거 등 로그아웃 처리 로직 진행
	}
}
