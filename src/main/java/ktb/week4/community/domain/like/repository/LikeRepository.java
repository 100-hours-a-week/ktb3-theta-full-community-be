package ktb.week4.community.domain.like.repository;

import ktb.week4.community.domain.like.entity.LikeArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeArticle, Long> {
    Optional<LikeArticle> findByUserIdAndArticleId(Long userId, Long articleId);
    boolean existsByUserIdAndArticleId(Long userId, Long articleId);
}
