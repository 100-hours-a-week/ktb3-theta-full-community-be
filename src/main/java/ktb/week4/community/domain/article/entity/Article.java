package ktb.week4.community.domain.article.entity;

import jakarta.persistence.*;
import ktb.week4.community.domain.user.entity.User;
import ktb.week4.community.global.common.BaseEntity;
import lombok.*;
import ktb.week4.community.domain.article.enums.PostTheme;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Article extends BaseEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String content;
	
	private String articleImage;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 32)
	private PostTheme theme = PostTheme.NONE;
	
	private int likeCount;
	private int viewCount;
	private int commentCount;
	
	private LocalDateTime deletedAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	public Article(String title, String content, String articleImage, PostTheme theme, User user) {
		this.title = title;
		this.content = content;
		this.articleImage = articleImage;
		this.theme = theme == null ? PostTheme.NONE : theme;
		this.user = user;
	}
	
	public void changeTitle(String title) {
		this.title = title;
	}
	
	public void changeContent(String content) {
		this.content = content;
	}
	
	public void changeArticleImage(String articleImage) {
		this.articleImage = articleImage;
	}
	
	public void increaseViewCount() {
		this.viewCount++;
	}
	
	public void increaseCommentCount() {
		this.commentCount++;
	}
	
	public void decreaseCommentCount() {
		this.commentCount--;
	}
	
	public void changeTheme(PostTheme theme) {
		this.theme = theme == null ? PostTheme.NONE : theme;
	}
	
	public void deleteArticle() {
		this.deletedAt = LocalDateTime.now();
	}
	
	@PrePersist
	@PreUpdate
	private void applyDefaultTheme() {
		if (this.theme == null) {
			this.theme = PostTheme.NONE;
		}
	}
}
