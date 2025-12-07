package ktb.week4.community.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import ktb.week4.community.domain.article.controller.ArticleController;
import ktb.week4.community.domain.article.dto.CreateArticleRequestDto;
import ktb.week4.community.domain.article.dto.UpdateArticleRequestDto;
import ktb.week4.community.domain.article.service.ArticleCommandService;
import ktb.week4.community.domain.article.service.ArticleQueryService;
import ktb.week4.community.global.config.SecurityConfig;
import ktb.week4.community.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(
		controllers = ArticleController.class,
		excludeAutoConfiguration = {
				HibernateJpaAutoConfiguration.class,
				JpaRepositoriesAutoConfiguration.class
		}
)
@Import({SecurityConfig.class, JwtTokenProvider.class})
@ActiveProfiles("test")
class ArticleControllerTest {
	
	@Autowired
	MockMvcTester mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockitoBean
	ArticleCommandService articleCommandService;
	
	@MockitoBean
	ArticleQueryService articleQueryService;
	
	@Test
	@DisplayName("createArticle이 multipart/form-data가 아니면 요청이 실패한다.")
	@WithMockUser
	void givenNonMultipart_whenCreateArticle_thenReturnsClientError() throws Exception {
		CreateArticleRequestDto dto = new CreateArticleRequestDto("title", "content", null);
		
		assertThat(mockMvc
				.post().uri("/articles")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		).hasStatus4xxClientError();
	}
	
	@Test
	@DisplayName("updateArticle이 multipart/form-data가 아니면 요청이 실패한다.")
	@WithMockUser
	void givenNonMultipart_whenUpdateArticle_thenReturnsClientError() throws Exception {
		UpdateArticleRequestDto dto = new UpdateArticleRequestDto("title", "content", null);
		
		assertThat(mockMvc
				.patch().uri("/articles/{articleId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		).hasStatus4xxClientError();
	}
}
