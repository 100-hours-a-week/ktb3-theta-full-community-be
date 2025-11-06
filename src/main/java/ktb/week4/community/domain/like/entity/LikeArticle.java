package ktb.week4.community.domain.like.entity;

import jakarta.persistence.*;
import ktb.week4.community.domain.article.entity.Article;
import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.global.common.BaseEntity;
import lombok.*;

@Entity
@Getter
@RequiredArgsConstructor
public class LikeArticle extends BaseEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id")
	private Article article;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	public LikeArticle(Article article, User user) {
		this.article = article;
		this.user = user;
	}
}
