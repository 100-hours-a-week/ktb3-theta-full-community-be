package ktb.week4.community.global.apiSpecification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import ktb.week4.community.domain.comment.dto.CommentResponseDto;
import ktb.week4.community.domain.comment.dto.CreateCommentRequestDto;
import ktb.week4.community.domain.comment.dto.GetCommentsResponseDto;
import ktb.week4.community.domain.comment.dto.UpdateCommentRequestDto;
import ktb.week4.community.global.apiPayload.ApiResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "Comment", description = "댓글 관련 API")
public interface CommentApiSpecification {
	
	@Operation(summary = "특정 게시글의 댓글을 조회합니다.")
	ApiResponse<GetCommentsResponseDto> getComments(
			@Parameter(description = "댓글을 조회할 게시글의 id", required = true, example = "1") Long articleId,
			@Parameter(description = "조회할 댓글의 페이지", required = true, example = "1") @Min(value = 1, message = "Page parameter must be at least 1") int page,
			@Parameter(description = "조회할 댓글의 페이지 당 사이즈", required = true, example = "7") @Min(value = 1, message = "Size parameter must be at least 1") int size);
	
	@Operation(summary = "특정 게시글에 댓글을 작성합니다.")
	ApiResponse<CommentResponseDto> createComment(
			@Parameter(description = "댓글을 조회할 게시글의 id", required = true, example = "1") Long articleId, Long userId,
			@Valid CreateCommentRequestDto request);
	
	@Operation(summary = "특정 댓글을 수정합니다.")
	ApiResponse<CommentResponseDto> updateComment(
			@Parameter(description = "수정할 댓글이 달린 게시글의 id", required = true, example = "1") Long articleId,
			@Parameter(description = "수정할 댓글의 id", required = true, example = "1") Long commentId,
			Long userId,
			@Valid UpdateCommentRequestDto request);
	
	@Operation(summary = "특정 댓글을 삭제합니다.")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
	})
	ResponseEntity<Void> deleteComment(
			@Parameter(description = "삭제할 댓글이 달린 게시글의 id", required = true, example = "1") Long articleId,
			@Parameter(description = "삭제할 댓글의 id", required = true, example = "1") Long commentId,
			Long userId);
}
