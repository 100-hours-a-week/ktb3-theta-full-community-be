package ktb.week4.community.comment;

import ktb.week4.community.domain.article.entity.Article;
import ktb.week4.community.domain.article.entity.ArticleTestBuilder;
import ktb.week4.community.domain.article.loader.ArticleLoader;
import ktb.week4.community.domain.comment.dto.GetCommentsResponseDto;
import ktb.week4.community.domain.comment.entity.Comment;
import ktb.week4.community.domain.comment.entity.CommentTestBuilder;
import ktb.week4.community.domain.comment.repository.CommentRepository;
import ktb.week4.community.domain.comment.service.CommentQueryService;
import ktb.week4.community.domain.user.dto.WrittenByResponseDto;
import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.domain.user.entity.UserTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentQueryServiceTest {
	
	@Mock
	CommentRepository commentRepository;
	
	@Mock
	ArticleLoader articleLoader;
	
	@InjectMocks
	CommentQueryService commentQueryService;
	
	@Test
	@DisplayName("댓글 목록 조회 시 최신 댓글이 하단에 정렬된다.")
	void givenComments_whenGetComments_thenNewestLast() {
		// given
		User user = UserTestBuilder.aUser().build();
		Article article = ArticleTestBuilder.anArticle().withUser(user).build();
		Comment older = spy(CommentTestBuilder.aComment().withUser(user).withArticle(article).build());
		Comment newer = spy(CommentTestBuilder.aComment().withUser(user).withArticle(article).build());
		LocalDateTime now = LocalDateTime.now();
		doReturn(now.minusHours(2)).when(older).getCreatedAt();
		doReturn(now).when(newer).getCreatedAt();
		
		when(articleLoader.getArticleById(1L)).thenReturn(article);
		when(commentRepository.findAllByArticleId(eq(1L), any(PageRequest.class)))
				.thenReturn(new PageImpl<>(List.of(older, newer), PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "createdAt")), 2));
		
		// when
		GetCommentsResponseDto res = commentQueryService.getComments(1L, 1, 10);
		
		// then
		assertThat(res).isNotNull();
		assertThat(res.comments()).hasSize(2);
		assertThat(res.comments().get(0).createdAt()).isBefore(res.comments().get(1).createdAt());
	}
	
	@Test
	@DisplayName("삭제된 유저의 댓글 조회 시 작성자 정보가 삭제된 이용자로 반환된다.")
	void givenDeletedUserComment_whenGetComments_thenWrittenByEmptyDto() {
		// given
		User deletedUser = UserTestBuilder.aUser().build();
		deletedUser.deleteUser();
		Article article = ArticleTestBuilder.anArticle().withUser(deletedUser).build();
		Comment deletedUserComment = CommentTestBuilder.aComment().withUser(deletedUser).withArticle(article).build();
		
		when(articleLoader.getArticleById(1L)).thenReturn(article);
		when(commentRepository.findAllByArticleId(eq(1L), any(PageRequest.class)))
				.thenReturn(new PageImpl<>(List.of(deletedUserComment), PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "createdAt")), 1));
		
		// when
		GetCommentsResponseDto res = commentQueryService.getComments(1L, 1, 10);
		
		// then
		assertThat(res.comments()).hasSize(1);
		assertThat(res.comments().getFirst().writtenBy()).isEqualTo(WrittenByResponseDto.emptyWrittenByDto());
	}
}
