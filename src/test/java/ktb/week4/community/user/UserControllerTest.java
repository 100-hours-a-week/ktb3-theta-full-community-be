package ktb.week4.community.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ktb.week4.community.domain.user.controller.UserController;
import ktb.week4.community.domain.user.dto.UpdateUserRequestDto;
import ktb.week4.community.domain.user.service.UserCommandService;
import ktb.week4.community.domain.user.service.UserQueryService;
import ktb.week4.community.global.config.SecurityConfig;
import ktb.week4.community.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
		controllers = UserController.class,
		excludeAutoConfiguration = {
				HibernateJpaAutoConfiguration.class,
				JpaRepositoriesAutoConfiguration.class
		}
)
@Import({SecurityConfig.class, JwtTokenProvider.class})
@ActiveProfiles("test")
class UserControllerTest {
	
	@Autowired
	MockMvcTester mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockitoBean
	UserCommandService userCommandService;
	
	@MockitoBean
	UserQueryService userQueryService;
	
	@Test
	@DisplayName("updateUser가 multipart/form-data가 아니면 요청에 실패한다.")
	@WithMockUser
	void givenNonMultipart_whenUpdateUser_thenReturnsClientError() throws Exception {
		UpdateUserRequestDto dto = new UpdateUserRequestDto("newNick", "newImage.png");
		
		assertThat(mockMvc
				.patch().uri("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		).hasStatus4xxClientError();
	}
}
