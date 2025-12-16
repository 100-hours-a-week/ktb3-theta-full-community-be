package ktb.week4.community.domain.article.repository;

import ktb.week4.community.domain.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
	Page<Article> findAllNotDeletedOrderByCreatedAtDesc(Pageable pageable);
}
