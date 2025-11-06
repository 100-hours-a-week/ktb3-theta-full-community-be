package ktb.week4.community.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ktb.week4.community.domain.comment.entity.Comment;
import ktb.week4.community.domain.user.dto.WrittenByResponseDto;
import ktb.week4.community.domain.user.entity.User;

import java.time.LocalDateTime;

public record CommentResponseDto(
        @JsonProperty("comment_id")
        Long commentId,
        String content,
        LocalDateTime createdAt,
        WrittenByResponseDto writtenBy
) {
    public static CommentResponseDto fromEntity(Comment comment, User user) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                WrittenByResponseDto.fromEntity(user)
        );
    }
	
	public static CommentResponseDto fromEntity(Comment comment) {
		return new CommentResponseDto(
				comment.getId(),
				comment.getContent(),
				comment.getCreatedAt(),
				WrittenByResponseDto.emptyWrittenByDto()
		);
	}
}
