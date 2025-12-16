package ktb.week4.community.domain.article.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ktb.week4.community.domain.article.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static ktb.week4.community.domain.article.entity.QArticle.article;
import static ktb.week4.community.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl  implements  ArticleRepositoryCustom{
	
	private final JPAQueryFactory jpaQueryFactory;
	
	@Override
	public Page<Article> findAllNotDeletedOrderByCreatedAtDesc(Pageable pageable) {
		List<Long> ids = jpaQueryFactory
				.select(article.id)
				.from(article)
				.where(article.deletedAt.isNull())
				.orderBy(article.createdAt.desc(), article.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		if(ids.isEmpty()) {
			return new PageImpl<>(Collections.emptyList());
		}
		
		List<Article> articles = jpaQueryFactory
				.selectFrom(article)
				.join(article.user, user).fetchJoin()
				.where(article.id.in(ids), article.deletedAt.isNull())
				.orderBy(article.createdAt.desc(), article.id.desc())
				.fetch();
		
		Long total = jpaQueryFactory
				.select(article.count())
				.from(article)
				.where(article.deletedAt.isNull())
				.fetchOne();
		
		return new PageImpl<>(articles, pageable, total == null ? 0 : total);
	}
}
