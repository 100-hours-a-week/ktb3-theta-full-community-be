package ktb.week4.community.domain.like.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LikeResponseDto(
        @JsonProperty("article_id")
        Long articleId,
		
        @JsonProperty("like_count")
        int likeCount,
		
        @JsonProperty("is_liked")
        boolean isLiked
) {
}
