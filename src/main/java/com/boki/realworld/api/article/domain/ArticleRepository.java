package com.boki.realworld.api.article.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    boolean existsArticleBySlug(String slug);

    Optional<Article> findArticleBySlug(String slug);

}