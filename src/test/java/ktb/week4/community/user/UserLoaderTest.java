package ktb.week4.community.user;

import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.domain.user.entity.UserTestBuilder;
import ktb.week4.community.domain.user.loader.UserLoader;
import ktb.week4.community.domain.user.repository.UserRepository;
import ktb.week4.community.global.apiPayload.ErrorCode;
import ktb.week4.community.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserLoaderTest {
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	UserLoader userLoader;
	
	@Test
	@DisplayName("존재하지 않는 사용자 조회 시 USER_NOT_FOUND 예외가 발생한다.")
	void givenUnknownId_whenGetUserById_thenThrowsUserNotFound() {
		// given
		when(userRepository.findById(999L)).thenReturn(Optional.empty());
		
		// when
		GeneralException exc = assertThrows(
				GeneralException.class,
				() -> userLoader.getUserById(999L)
		);
		
		// then
		assertEquals(ErrorCode.USER_NOT_FOUND, exc.getErrorCode());
	}
	
	@Test
	@DisplayName("사용자 ID로 조회 성공 시 User 반환")
	void givenExistingId_whenGetUserById_thenReturnsUser() {
		// given
		User user = UserTestBuilder.aUser().build();
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		
		// when
		User result = userLoader.getUserById(1L);
		
		// then
		assertEquals(user, result);
	}
	
	@Test
	@DisplayName("이메일로 사용자 조회 성공 시 User를 반환한다.")
	void givenExistingEmail_whenGetUserByEmail_thenReturnsUser() {
		// given
		User user = UserTestBuilder.aUser().build();
		when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
		
		// when
		User result = userLoader.getUserByEmail("test@test.com");
		
		// then
		assertEquals(user, result);
	}
	
	@Test
	@DisplayName("이메일로 사용자 조회 실패 시 _BAD_REQUEST 예외 발생")
	void givenUnknownEmail_whenGetUserByEmail_thenThrowsBadRequest() {
		// given
		when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());
		
		// when
		GeneralException exc = assertThrows(
				GeneralException.class,
				() -> userLoader.getUserByEmail("missing@test.com")
		);
		
		// then
		assertEquals(ErrorCode._BAD_REQUEST, exc.getErrorCode());
	}
	
	@Test
	@DisplayName("ID 목록 조회 시 ID - User 맵을 반환한다.")
	void givenIds_whenGetUsersByIds_thenReturnsMap() {
		// given
		User user1 = spy(UserTestBuilder.aUser().build());
		User user2 = spy(UserTestBuilder.aUser().build());
		doReturn(1L).when(user1).getId();
		doReturn(2L).when(user2).getId();
		when(userRepository.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of(user1, user2));
		
		// when
		var result = userLoader.getUsersByIds(List.of(1L, 2L));
		
		// then
		assertEquals(2, result.size());
		assertEquals(user1, result.get(user1.getId()));
		assertEquals(user2, result.get(user2.getId()));
	}
}
