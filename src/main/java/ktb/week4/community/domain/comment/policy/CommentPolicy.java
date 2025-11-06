package ktb.week4.community.domain.comment.policy;

import ktb.week4.community.domain.comment.entity.Comment;
import ktb.week4.community.global.apiPayload.ErrorCode;
import ktb.week4.community.global.exception.GeneralException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentPolicy {
	
	public void checkWrittenBy(Comment comment, Long userId) {
		if (!comment.getUser().getId().equals(userId)) {
			throw new GeneralException(ErrorCode.FORBIDDEN_REQUEST);
		}
	}
	
	public void checkBelongsTo(Comment comment, Long articleId) {
		if(!comment.getArticle().getId().equals(articleId)) {
			throw new GeneralException(ErrorCode.COMMENT_NOT_BELONG_TO_ARTICLE);
		}
	}
}
