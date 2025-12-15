package ktb.week4.community.domain.like.repository;

import ktb.week4.community.domain.like.entity.LikeArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeArticle, Long> {
    boolean existsByUserIdAndArticleId(Long userId, Long articleId);
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = """
		INSERT into like_article(article_id, user_id, created_at)
			values (:articleId, :userId, NOW())
			ON DUPLICATE KEY UPDATE id = id
	""", nativeQuery = true)
	int insertLikeIgnoringDuplication(@Param("articleId" ) Long articleId, @Param("userId") Long userId);
	
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = """
		DELETE from like_article
			       where article_id = :articleId and user_id = :userId
	""", nativeQuery = true)
	int deleteLikeIgnoringDuplication(@Param("articleId" ) Long articleId, @Param("userId") Long userId);
}
