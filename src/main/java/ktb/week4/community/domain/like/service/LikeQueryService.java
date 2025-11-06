package ktb.week4.community.domain.like.service;

import ktb.week4.community.domain.article.entity.Article;
import ktb.week4.community.domain.like.dto.LikeResponseDto;
import ktb.week4.community.domain.like.repository.LikeRepository;
import ktb.week4.community.domain.article.loader.ArticleLoader;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LikeQueryService {

    private final LikeRepository likeRepository;
	private final ArticleLoader articleLoader;

    public LikeResponseDto getLikeStatus(Long articleId, Long userId) {
        Article article = articleLoader.getArticleById(articleId);

        boolean isLiked = likeRepository.existsByUserIdAndArticleId(userId, articleId);

        return new LikeResponseDto(articleId, article.getLikeCount(), isLiked);
    }
}
