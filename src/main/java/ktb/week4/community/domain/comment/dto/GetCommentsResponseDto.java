package ktb.week4.community.domain.comment.dto;

import java.util.List;

public record GetCommentsResponseDto(
        List<CommentResponseDto> comments,
        int currentPage,
        long totalCount,
        int totalPages,
        boolean isLast
) {
}
