package ktb.week4.community.domain.comment.entity;

import jakarta.persistence.*;
import ktb.week4.community.domain.article.entity.Article;
import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.global.common.BaseEntity;
import lombok.*;

@Entity
@Getter
@RequiredArgsConstructor
public class Comment extends BaseEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id")
	private Article article;

	public Comment(String content, User user, Article article) {
		this.content = content;
		this.user = user;
		this.article = article;
	}
	
	public void changeContent(String content) {
		this.content = content;
	}
}
