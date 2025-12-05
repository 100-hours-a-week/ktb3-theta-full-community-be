package ktb.week4.community.user;

import ktb.week4.community.domain.user.dto.SignUpRequestDto;
import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.domain.user.entity.UserTestBuilder;
import ktb.week4.community.domain.user.repository.UserRepository;
import ktb.week4.community.domain.user.service.UserCommandService;
import ktb.week4.community.domain.user.validator.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	UserRepository userRepository;
	
	@Mock
	UserValidator userValidator;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@InjectMocks
	UserCommandService userCommandService;

	@Test
	@DisplayName("정상적인 SignUpRequest를 받으면 회원가입 한 유저 저장")
	void givenValidSignUpRequest_whenCreateUser_thenSavesUser() {
		
		// Arrange
		SignUpRequestDto validRequest = new SignUpRequestDto(
				"valid@test.com",
				"Aa12345!!",
				"테스트 유저",
				null);
		
		when(passwordEncoder.encode(validRequest.password())).thenReturn(validRequest.password());
		User user = spy(UserTestBuilder.aUser()
				.build());
		doReturn(1L).when(user).getId();
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		// Act
		Long userId = userCommandService.createUser(validRequest).user_id();
		
		// Assert/Verify
		assertEquals(user.getId(), userId);
	}
}
