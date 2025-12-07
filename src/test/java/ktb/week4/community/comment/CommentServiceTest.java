package ktb.week4.community.comment;

import ktb.week4.community.domain.article.entity.Article;
import ktb.week4.community.domain.article.entity.ArticleTestBuilder;
import ktb.week4.community.domain.article.loader.ArticleLoader;
import ktb.week4.community.domain.article.repository.ArticleRepository;
import ktb.week4.community.domain.comment.dto.CommentResponseDto;
import ktb.week4.community.domain.comment.dto.CreateCommentRequestDto;
import ktb.week4.community.domain.comment.dto.UpdateCommentRequestDto;
import ktb.week4.community.domain.comment.entity.Comment;
import ktb.week4.community.domain.comment.entity.CommentTestBuilder;
import ktb.week4.community.domain.comment.loader.CommentLoader;
import ktb.week4.community.domain.comment.policy.CommentPolicy;
import ktb.week4.community.domain.comment.repository.CommentRepository;
import ktb.week4.community.domain.comment.service.CommentCommandService;
import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.domain.user.entity.UserTestBuilder;
import ktb.week4.community.domain.user.loader.UserLoader;
import ktb.week4.community.global.apiPayload.ErrorCode;
import ktb.week4.community.global.exception.GeneralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
	
	@Mock
	CommentRepository commentRepository;
	
	@Mock
	UserLoader userLoader;
	
	@Mock
	ArticleLoader articleLoader;
	
	@Mock
	CommentLoader commentLoader;
	
	@Mock
	CommentPolicy commentPolicy;
	
	@Mock
	ArticleRepository articleRepository;
	
	@InjectMocks
	CommentCommandService commentCommandService;
	
	User author;
	User user;
	Article article;
	Comment comment;
	
	@BeforeEach
	void setUp() {
		author = spy(UserTestBuilder.aUser()
				.withEmail("author@test.com")
				.withNickname("author")
				.build());
		
		user = spy(UserTestBuilder.aUser()
				.withEmail("other@test.com")
				.withNickname("user")
				.build());
		
		article = spy(ArticleTestBuilder.anArticle()
				.withUser(author)
				.build());
		
		comment = spy(CommentTestBuilder.aComment()
				.withUser(author)
				.withArticle(article)
				.build());
	}
	
	@Test
	@DisplayName("댓글 작성 성공 시 CommentResponseDto를 반환하고 게시글 댓글 수가 1 증가한다.")
	void givenValidRequest_whenCreateComment_thenSucceeds() {
		// given
		Long authorId = 1L;
		Long articleId = 10L;
		CreateCommentRequestDto request = new CreateCommentRequestDto("댓글 내용입니다.");
		Comment savedComment = spy(new Comment(request.content(), author, article));
		doReturn(1L).when(savedComment).getId();
		
		when(userLoader.getUserById(authorId)).thenReturn(author);
		when(articleLoader.getArticleById(articleId)).thenReturn(article);
		when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);
		
		// when
		CommentResponseDto response = commentCommandService.createComment(
				authorId, articleId, request
		);
		
		// then
		assertThat(response).isNotNull();
		assertEquals(savedComment.getId(), response.commentId());
		assertEquals(request.content(), response.content());
		assertThat(article.getCommentCount()).isEqualTo(1);
		verify(commentRepository).save(any(Comment.class));
		verify(articleRepository).save(article);
	}
	
	@Test
	@DisplayName("댓글 작성자가 댓글을 수정하면 내용이 변경된다.")
	void givenAuthor_whenModifyComment_thenSucceeds() {
		// given
		Long authorId = 1L;
		Long articleId = 10L;
		Long commentId = 100L;
		UpdateCommentRequestDto request = new UpdateCommentRequestDto("수정된 댓글 내용입니다.");
		
		when(commentLoader.getCommentById(commentId)).thenReturn(comment);
		when(userLoader.getUserById(authorId)).thenReturn(author);
		when(commentRepository.save(comment)).thenReturn(comment);
		
		// when
		CommentResponseDto response = commentCommandService.updateComment(
				articleId, authorId, commentId, request
		);
		
		// then
		assertThat(response).isNotNull();
		assertEquals(request.content(), comment.getContent());
		assertEquals(request.content(), response.content());
		verify(commentPolicy).checkWrittenBy(comment, authorId);
		verify(commentPolicy).checkBelongsTo(comment, articleId);
	}
	
	@Test
	@DisplayName("댓글 작성자가 아니라면 댓글을 수정할 수 없다.")
	void givenAuthor_whenModifyComment_thenThrowForbidden() {
		// given
		UpdateCommentRequestDto request = new UpdateCommentRequestDto("수정된 댓글 내용입니다.");
		
		when(commentLoader.getCommentById(100L)).thenReturn(comment);
		doThrow(new GeneralException(ErrorCode.FORBIDDEN_REQUEST))
				.when(commentPolicy)
				.checkWrittenBy(comment, 2L);
		
		// when
		GeneralException exc = assertThrows(
				GeneralException.class,
				() -> commentCommandService.updateComment(
						10L, 2L, 100L, request
				)
		);
		
		// then
		assertEquals(ErrorCode.FORBIDDEN_REQUEST, exc.getErrorCode());
		verify(commentRepository, never()).save(any());
	}
	
	@Test
	@DisplayName("댓글 작성자가 삭제하면 댓글이 삭제되고 게시글 댓글 수가 1 감소한다.")
	void givenAuthor_whenDeleteComment_thenSucceeds() {
		// given
		article.increaseCommentCount();
		
		when(commentLoader.getCommentById(100L)).thenReturn(comment);
		when(articleLoader.getArticleById(10L)).thenReturn(article);
		
		// when
		commentCommandService.deleteComment(10L, 1L, 100L);
		
		// then
		assertThat(article.getCommentCount()).isZero();
		verify(commentRepository).delete(comment);
		verify(articleRepository).save(article);
	}
	
	@Test
	@DisplayName("댓글 작성자가 아니라면 댓글을 삭제할 수 없다.")
	void givenAuthor_whenDeleteComment_thenThrowForbidden() {
		// given
		article.increaseCommentCount();
		
		when(commentLoader.getCommentById(100L)).thenReturn(comment);
		when(articleLoader.getArticleById(10L)).thenReturn(article);
		doThrow(new GeneralException(ErrorCode.FORBIDDEN_REQUEST))
				.when(commentPolicy)
				.checkWrittenBy(comment, 2L);
		
		// when
		GeneralException exc = assertThrows(
				GeneralException.class,
				() -> commentCommandService.deleteComment(10L, 2L, 100L)
		);
		
		// then
		assertEquals(ErrorCode.FORBIDDEN_REQUEST, exc.getErrorCode());
		assertThat(article.getCommentCount()).isEqualTo(1);
		verify(commentRepository, never()).delete(any());
		verify(articleRepository, never()).save(any(Article.class));
	}
}
