package com.boki.realworld.api.article.dto.response;

import com.boki.realworld.api.article.domain.Article;
import com.boki.realworld.api.article.dto.response.SingleArticleResponse.ArticleInfo;
import com.boki.realworld.api.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MultipleArticleResponse {

    private List<ArticleInfo> articles;
    private int articlesCount;

    public MultipleArticleResponse(List<ArticleInfo> articles) {
        this.articles = articles;
        this.articlesCount = articles.size();
    }

    public static MultipleArticleResponse of(List<Article> articles, User user) {
        List<ArticleInfo> multipleArticles = articles.stream()
            .map(article -> ArticleInfo.of(article, user))
            .collect(Collectors.toList());
        return new MultipleArticleResponse(multipleArticles);
    }
}