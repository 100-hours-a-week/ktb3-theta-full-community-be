package ktb.week4.community.user;

import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.domain.user.entity.UserTestBuilder;
import ktb.week4.community.domain.user.policy.UserPolicy;
import ktb.week4.community.global.apiPayload.ErrorCode;
import ktb.week4.community.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserPolicyTest {
	
	@InjectMocks
	UserPolicy userPolicy;
	
	@Test
	@DisplayName("비밀번호가 다르면 BAD_REQUEST 예외가 발생한다.")
	void givenWrongPassword_whenMatchPassword_thenThrowsBadRequest() {
		// given
		User user = UserTestBuilder.aUser()
				.withPassword("correct")
				.build();
		
		// when
		GeneralException exc = assertThrows(
				GeneralException.class,
				() -> userPolicy.matchPassword(user, "wrong")
		);
		
		// then
		assertEquals(ErrorCode._BAD_REQUEST, exc.getErrorCode());
	}
	
	@Test
	@DisplayName("비밀번호가 일치하면 검증을 통과한다.")
	void givenCorrectPassword_whenMatchPassword_thenPasses() {
		// given
		User user = UserTestBuilder.aUser()
				.withPassword("1234")
				.build();
		
		// expect
		assertDoesNotThrow(() -> userPolicy.matchPassword(user, "1234"));
	}
}
