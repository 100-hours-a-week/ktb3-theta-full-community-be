package ktb.week4.community.article;

import ktb.week4.community.domain.article.dto.GetArticlesResponseDto;
import ktb.week4.community.domain.article.entity.Article;
import ktb.week4.community.domain.article.entity.ArticleTestBuilder;
import ktb.week4.community.domain.article.repository.ArticleRepository;
import ktb.week4.community.domain.article.service.ArticleQueryService;
import ktb.week4.community.domain.user.dto.WrittenByResponseDto;
import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.domain.user.entity.UserTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ArticleQueryServiceTest {
	
	@Mock
	ArticleRepository articleRepository;
	
	@InjectMocks
	ArticleQueryService articleQueryService;
	
	@Test
	@DisplayName("게시글 목록 조회 시 최신 게시글이 상단에 정렬된다.")
	void givenArticles_whenGetArticles_thenNewestFirst() {
		// given
		User user = UserTestBuilder.aUser().build();
		Article older = spy(ArticleTestBuilder.anArticle().withUser(user).build());
		Article newer = spy(ArticleTestBuilder.anArticle().withUser(user).build());
		LocalDateTime now = LocalDateTime.now();
		doReturn(now.minusDays(1)).when(older).getCreatedAt();
		doReturn(now).when(newer).getCreatedAt();
		
		when(articleRepository.findAllByOrderByCreatedAtDesc(any(PageRequest.class)))
				.thenReturn(new PageImpl<>(List.of(newer, older), PageRequest.of(0, 10), 2));
		
		// when
		GetArticlesResponseDto res = articleQueryService.getArticles(1, 10);
		
		// then
		assertThat(res).isNotNull();
		assertThat(res.articles()).hasSize(2);
		assertThat(res.articles().get(0).createdAt()).isAfter(res.articles().get(1).createdAt());
	}
	
	@Test
	@DisplayName("삭제된 유저의 게시글 조회 시 작성자 정보가 삭제된 이용자로 반환된다.")
	void givenDeletedUser_whenGetArticles_thenWrittenByEmptyDto() {
		// given
		User deletedUser = UserTestBuilder.aUser().build();
		deletedUser.deleteUser();
		Article article = ArticleTestBuilder.anArticle().withUser(deletedUser).build();
		
		when(articleRepository.findAllByOrderByCreatedAtDesc(any(PageRequest.class)))
				.thenReturn(new PageImpl<>(List.of(article), PageRequest.of(0, 10), 1));
		
		// when
		GetArticlesResponseDto res = articleQueryService.getArticles(1, 10);
		
		// then
		assertThat(res.articles()).hasSize(1);
		assertThat(res.articles().getFirst().writtenBy()).isEqualTo(WrittenByResponseDto.emptyWrittenByDto());
	}
}
