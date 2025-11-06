package ktb.week4.community.domain.comment.loader;

import ktb.week4.community.domain.comment.entity.Comment;
import ktb.week4.community.domain.comment.repository.CommentRepository;
import ktb.week4.community.global.apiPayload.ErrorCode;
import ktb.week4.community.global.exception.GeneralException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentLoader {
	private final CommentRepository commentRepository;
	
	public Comment getCommentById(Long commentId) {
		return commentRepository.findById(commentId).orElseThrow(() -> new GeneralException(ErrorCode.COMMENT_NOT_FOUND));
	}
}
