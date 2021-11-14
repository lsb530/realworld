package com.boki.realworld.api.article.domain;

import static com.boki.realworld.api.article.domain.QArticle.article;
import static com.boki.realworld.api.tag.domain.QTag.tag;
import static com.boki.realworld.api.user.domain.QUser.user;

import com.boki.realworld.api.article.dto.condition.ArticleSearchCondition;
import com.boki.realworld.api.user.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ArticleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Article> findAll(ArticleSearchCondition condition) {
        return queryFactory
            .selectDistinct(article)
            .from(article)
            .leftJoin(article.author, user)
            .leftJoin(article.tagList, tag)
            .leftJoin(article.favoriteList, user)
            .where(
                eqAuthor(condition.getAuthor()),
                eqFavorite(condition.getFavorited()),
                eqTag(condition.getTag())
            )
            .limit(condition.getLimit())
            .offset(condition.getOffset())
            .orderBy(article.updatedAt.desc())
            .orderBy(article.createdAt.desc())
            .fetch();
    }

    public List<Article> findFeedArticles(ArticleSearchCondition condition, Set<User> followList) {
        if(followList.isEmpty()) return Collections.emptyList();
        return queryFactory
            .selectDistinct(article)
            .from(article)
            .where(article.author.in(followList))
            .limit(condition.getLimit())
            .offset(condition.getOffset())
            .orderBy(article.updatedAt.desc())
            .orderBy(article.createdAt.desc())
            .fetch();
    }

    private BooleanExpression eqAuthor(String author) {
        return StringUtils.isBlank(author) ? null : article.author.username.eq(author);
    }

    private BooleanExpression eqFavorite(String favorited) {
        return StringUtils.isBlank(favorited) ? null : user.username.eq(favorited);
    }

    private BooleanExpression eqTag(String tagName) {
        return StringUtils.isBlank(tagName) ? null : tag.name.eq(tagName);
    }
}