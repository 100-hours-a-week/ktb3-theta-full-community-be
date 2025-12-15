package ktb.week4.community.domain.article.repository;

import ktb.week4.community.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE Article a SET a.likeCount = a.likeCount + :count  where a.id = :articleId")
	void updateLikeCount(@Param("articleId") Long articleId, @Param("count") int count);
}