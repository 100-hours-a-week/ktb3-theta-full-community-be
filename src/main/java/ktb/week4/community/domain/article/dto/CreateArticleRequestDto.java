package ktb.week4.community.domain.article.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateArticleRequestDto(
		@Schema(description = "게시글 제목", example = "글 제목입니다.")
		@NotBlank(message = "게시글 제목은 필수입니다.")
		String title,
		
		@Schema(description = "게시글 본문", example = "게시글 본문 내용입니다.")
		@NotBlank(message = "게시글 본문은 필수입니다.")
		String content,
		
		@Schema(description = "게시글 내 이미지", example = "게시글 내에 이미지가 있는 경우, url을 첨부해주세요.")
		@JsonProperty("article_image")
		String articleImage
) {
}