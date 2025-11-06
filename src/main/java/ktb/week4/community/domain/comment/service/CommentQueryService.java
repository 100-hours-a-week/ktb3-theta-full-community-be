package ktb.week4.community.domain.comment.service;

import ktb.week4.community.domain.article.loader.ArticleLoader;
import ktb.week4.community.domain.comment.dto.CommentResponseDto;
import ktb.week4.community.domain.comment.dto.GetCommentsResponseDto;
import ktb.week4.community.domain.comment.entity.Comment;
import ktb.week4.community.domain.comment.repository.CommentRepository;
import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.domain.user.loader.UserLoader;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {
	
	private final CommentRepository commentRepository;
	private final UserLoader userLoader;
	private final ArticleLoader articleLoader;
	
	public GetCommentsResponseDto getComments(Long articleId, int page, int size) {
		articleLoader.getArticleById(articleId);
		
		Page<Comment> comments = commentRepository.findAllByArticleId(articleId, PageRequest.of(page-1, size));
		
		List<Long> userIds = comments.stream()
				.map(Comment::getUser)
				.distinct()
				.map(User::getId)
				.toList();
		
		Map<Long, User> userMap = userLoader.getUsersByIds(userIds);
		
		List<CommentResponseDto> responses = comments.stream()
				.map(comment -> {
					User user = userMap.get(comment.getUser().getId());
					if (user == null) {
						return CommentResponseDto.fromEntity(comment);
					}
					return CommentResponseDto.fromEntity(comment, user);
				})
				.collect(Collectors.toList());
		
		return new GetCommentsResponseDto(responses,
				page,
				comments.getTotalElements(),
				comments.getTotalPages(),
				comments.isLast());
	}
}