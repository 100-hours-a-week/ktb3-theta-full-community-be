package ktb.week4.community.domain.user.policy;

import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.global.apiPayload.ErrorCode;
import ktb.week4.community.global.exception.GeneralException;
import org.springframework.stereotype.Component;

@Component
public class UserPolicy {
	
	public void matchPassword(User user, String password) {
		if (!password.equals(user.getPassword())) {
			throw new GeneralException(ErrorCode._BAD_REQUEST);
		}
	}
}
