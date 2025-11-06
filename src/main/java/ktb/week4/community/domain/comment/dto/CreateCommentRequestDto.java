package ktb.week4.community.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequestDto(
		@Schema(description = "댓글 내용", example = "댓글 내용입니다.")
		@NotBlank(message = "댓글에 내용이 있어야 합니다.")
        String content
) {
}
