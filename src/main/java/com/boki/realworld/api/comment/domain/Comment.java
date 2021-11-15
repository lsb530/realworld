package com.boki.realworld.api.comment.domain;

import static javax.persistence.FetchType.LAZY;

import com.boki.realworld.api.article.domain.Article;
import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.common.BaseTimeEntity;
import com.boki.realworld.common.exception.IllegalParameterException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "comments")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    @Lob
    private String body;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @Builder
    public Comment(LocalDateTime createdAt, LocalDateTime updatedAt,
        Long id, String body, User author, Article article) {
        super(createdAt, updatedAt);
        validateGenerateParams(body, article, author);
        this.id = id;
        this.body = body;
        this.author = author;
        this.article = article;
    }

    private void validateGenerateParams(String body, Article article, User author) {
        if (!StringUtils.hasText(body) || ObjectUtils.isEmpty(article)
            || ObjectUtils.isEmpty(author)) {
            throw new IllegalParameterException("comment");
        }
    }

}