package ktb.week4.community.domain.like.service;

import ktb.week4.community.domain.article.entity.Article;
import ktb.week4.community.domain.article.repository.ArticleRepository;
import ktb.week4.community.domain.like.dto.LikeResponseDto;
import ktb.week4.community.domain.like.repository.LikeRepository;
import ktb.week4.community.domain.article.loader.ArticleLoader;
import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.domain.user.loader.UserLoader;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class LikeCommandService {

    private final LikeRepository likeRepository;
	private final ArticleLoader articleLoader;
	private final ArticleRepository articleRepository;
	private final UserLoader userLoader;
	
	public LikeResponseDto likeArticle(Long articleId, Long userId) {
		Article article = articleLoader.getArticleById(articleId);
		User user = userLoader.getUserById(userId);
		
		int updated = 	likeRepository.insertLikeIgnoringDuplication(article.getId(), user.getId());
		if(updated <= 0) {
			return new LikeResponseDto(articleId, article.getLikeCount(), true);
		}
		
		articleRepository.updateLikeCount(articleId, 1);
        return new LikeResponseDto(articleId, article.getLikeCount(), true);
    }

    public void unlikeArticle(Long articleId, Long userId) {
		Article article = articleLoader.getArticleById(articleId);
		
		if(likeRepository.deleteLikeIgnoringDuplication(article.getId(), userId) == 1) {
			articleRepository.updateLikeCount(articleId, -1);
		}
    }
}
