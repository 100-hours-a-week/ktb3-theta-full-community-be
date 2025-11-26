package ktb.week4.community.user;

import ktb.week4.community.domain.user.repository.UserRepository;
import ktb.week4.community.domain.user.validator.UserValidator;
import ktb.week4.community.global.apiPayload.ErrorCode;
import ktb.week4.community.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	UserValidator userValidator;
	
	@Test
	@DisplayName("이미 존재하는 이메일이면 EMAIL_ALREADY_EXISTS 예외 발생")
	void shouldReturnExceptionIfEmailExists() {
		// given
		when(userRepository.existsByEmail("email@e.com")).thenReturn(true);
		
		// when
		GeneralException exc = assertThrows(
				GeneralException.class,
				() -> userValidator.validateEmailIsNotTaken("email@e.com")
		);
		
		// then
		assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS, exc.getErrorCode());
	}
	
	@Test
	@DisplayName("이미 존재하는 닉네임이면 NICKNAME_ALREADY_EXISTS 예외 발생")
	void shouldReturnExceptionIfNicknameExists() {
		// given
		when(userRepository.existsByNickname("User")).thenReturn(true);
		
		// when
		GeneralException exc = assertThrows(
				GeneralException.class,
				() -> userValidator.validateNicknameIsNotTaken("User")
		);
		
		// then
		assertEquals(ErrorCode.NICKNAME_ALREADY_EXISTS, exc.getErrorCode());
	}
}
