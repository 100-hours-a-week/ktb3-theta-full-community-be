package ktb.week4.community.domain.user.loader;

import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.domain.user.repository.UserRepository;
import ktb.week4.community.global.apiPayload.ErrorCode;
import ktb.week4.community.global.exception.GeneralException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserLoader {
	private final UserRepository userRepository;
	
	public User getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
	}
	
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new GeneralException(ErrorCode._BAD_REQUEST));
	}

	public Map<Long, User> getUsersByIds(List<Long> userIds) {
		return userRepository.findAllByIdIn(userIds).stream()
				.collect(Collectors.toMap(User::getId, Function.identity()));
	}
	
}
