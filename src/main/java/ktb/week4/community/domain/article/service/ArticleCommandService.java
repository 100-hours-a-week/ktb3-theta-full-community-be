package ktb.week4.community.domain.article.service;

import ktb.week4.community.domain.article.dto.ArticleResponseDto;
import ktb.week4.community.domain.article.dto.CreateArticleRequestDto;
import ktb.week4.community.domain.article.dto.UpdateArticleRequestDto;
import ktb.week4.community.domain.article.entity.Article;
import ktb.week4.community.domain.article.policy.ArticlePolicy;
import ktb.week4.community.domain.article.repository.ArticleRepository;
import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.domain.article.loader.ArticleLoader;
import ktb.week4.community.domain.user.loader.UserLoader;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class ArticleCommandService {
	
	private final ArticleRepository articleRepository;
	private final UserLoader userLoader;
	private final ArticleLoader articleLoader;
	private final ArticlePolicy articlePolicy;
	
	public ArticleResponseDto createArticle(Long userId, CreateArticleRequestDto request) {
		User writtenBy = userLoader.getUserById(userId);
		
		Article article = articleRepository.save(
				new Article(
						request.title(),
						request.content(),
						request.articleImage(),
						writtenBy
				)
		);
		
		return ArticleResponseDto.fromEntity(article, writtenBy);
	}
	
	public ArticleResponseDto updateArticle(Long userId, Long articleId, UpdateArticleRequestDto request) {
		User writtenBy = userLoader.getUserById(userId);
		Article article = articleLoader.getArticleById(articleId);
		
		articlePolicy.checkWrittenBy(article, userId);
		
		if(!request.title().isEmpty()) {
			article.changeTitle(request.title());
		}
		if(!request.content().isEmpty()) {
			article.changeContent(request.content());
		}
		if(!request.articleImage().isEmpty()) {
			article.changeArticleImage(request.articleImage());
		}
		
		Article updatedArticle = articleRepository.save(article);
		return ArticleResponseDto.fromEntity(updatedArticle, writtenBy);
	}
	
	public void deleteArticle(Long userId, Long articleId) {
		Article article = articleLoader.getArticleById(articleId);
		articlePolicy.checkWrittenBy(article, userId);
		
		article.deleteArticle();
		articleRepository.save(article);
	}
}
