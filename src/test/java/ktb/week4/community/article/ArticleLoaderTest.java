package ktb.week4.community.article;

import ktb.week4.community.domain.article.loader.ArticleLoader;
import ktb.week4.community.domain.article.repository.ArticleRepository;
import ktb.week4.community.global.apiPayload.ErrorCode;
import ktb.week4.community.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleLoaderTest {
	
	@Mock
	ArticleRepository articleRepository;
	
	@InjectMocks
	ArticleLoader articleLoader;
	
	@Test
	@DisplayName(("존재하지 않는 글을 조회하려 하면 ARTICLE_NOT_FOUND를 받는다."))
	void givenNotExistingArticle_whenValidateArticleExists_thenThrowsArticleNotFoundException() {
		// given
		when(articleRepository.findById(999L)).thenReturn(Optional.empty());
		
		// when
		GeneralException exc = assertThrows(
				GeneralException.class,
				() -> articleLoader.getArticleById(999L)
		);
		
		// then
		assertEquals(ErrorCode.ARTICLE_NOT_FOUND, exc.getErrorCode());
	}
}
