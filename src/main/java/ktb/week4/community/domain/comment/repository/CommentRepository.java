package ktb.week4.community.domain.comment.repository;

import ktb.week4.community.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
   Page<Comment> findAllByArticleId(Long articleId, Pageable pageable);
}