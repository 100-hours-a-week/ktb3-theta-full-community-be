package ktb.week4.community.domain.article.policy;

import ktb.week4.community.domain.article.entity.Article;
import ktb.week4.community.global.apiPayload.ErrorCode;
import ktb.week4.community.global.exception.GeneralException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ArticlePolicy {
	
	public void checkWrittenBy(Article article, Long userId) {
		if (!article.getUser().getId().equals(userId)) {
			throw new GeneralException(ErrorCode.FORBIDDEN_REQUEST);
		}
	}
}
