package ktb.week4.community.domain.article.dto;

import java.util.List;

public record GetArticlesResponseDto(
		
		List<ArticleResponseDto> articles,
		long currentPage,
		long totalCount,
		long totalPages,
		boolean isLast
) {
}
