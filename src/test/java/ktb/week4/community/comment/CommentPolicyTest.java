package ktb.week4.community.comment;

import ktb.week4.community.domain.article.entity.Article;
import ktb.week4.community.domain.article.entity.ArticleTestBuilder;
import ktb.week4.community.domain.comment.entity.Comment;
import ktb.week4.community.domain.comment.entity.CommentTestBuilder;
import ktb.week4.community.domain.comment.policy.CommentPolicy;
import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.domain.user.entity.UserTestBuilder;
import ktb.week4.community.global.apiPayload.ErrorCode;
import ktb.week4.community.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class CommentPolicyTest {
	
	@InjectMocks
	CommentPolicy commentPolicy;
	
	@Test
	@DisplayName("댓글 작성자가 아니면 수정/삭제 불가")
	void givenNotAuthor_whenCheckWrittenBy_thenThrowsForbidden() {
		// given
		User author = spy(UserTestBuilder.aUser().build());
		doReturn(1L).when(author).getId();
		User other = spy(UserTestBuilder.aUser().build());
		doReturn(2L).when(other).getId();
		
		Comment comment = CommentTestBuilder.aComment()
				.withUser(author)
				.build();
		
		// when
		GeneralException exc = assertThrows(
				GeneralException.class,
				() -> commentPolicy.checkWrittenBy(comment, other.getId())
		);
		
		// then
		assertEquals(ErrorCode.FORBIDDEN_REQUEST, exc.getErrorCode());
	}
	
	@Test
	@DisplayName("댓글 작성자면 수정/삭제 검증 통과")
	void givenAuthor_whenCheckWrittenBy_thenPasses() {
		// given
		User author = spy(UserTestBuilder.aUser().build());
		doReturn(1L).when(author).getId();
		
		Comment comment = CommentTestBuilder.aComment()
				.withUser(author)
				.build();
		
		// expect
		assertDoesNotThrow(() -> commentPolicy.checkWrittenBy(comment, author.getId()));
	}
	
	@Test
	@DisplayName("댓글이 다른 게시글에 속하면 COMMENT_NOT_BELONG_TO_ARTICLE 예외")
	void givenDifferentArticle_whenCheckBelongsTo_thenThrowsCommentNotBelongToArticle() {
		// given
		Article targetArticle = spy(ArticleTestBuilder.anArticle().build());
		doReturn(1L).when(targetArticle).getId();
		Article otherArticle = spy(ArticleTestBuilder.anArticle().build());
		doReturn(2L).when(otherArticle).getId();
		
		Comment comment = CommentTestBuilder.aComment()
				.withArticle(otherArticle)
				.build();
		
		// when
		GeneralException exc = assertThrows(
				GeneralException.class,
				() -> commentPolicy.checkBelongsTo(comment, targetArticle.getId())
		);
		
		// then
		assertEquals(ErrorCode.COMMENT_NOT_BELONG_TO_ARTICLE, exc.getErrorCode());
	}
	
	@Test
	@DisplayName("댓글이 해당 게시글에 속하면 검증 통과")
	void givenSameArticle_whenCheckBelongsTo_thenPasses() {
		// given
		Article article = spy(ArticleTestBuilder.anArticle().build());
		doReturn(1L).when(article).getId();
		
		Comment comment = CommentTestBuilder.aComment()
				.withArticle(article)
				.build();
		
		// expect
		assertDoesNotThrow(() -> commentPolicy.checkBelongsTo(comment, 1L));
	}
}
