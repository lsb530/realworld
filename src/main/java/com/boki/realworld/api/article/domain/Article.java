package com.boki.realworld.api.article.domain;

import static javax.persistence.FetchType.LAZY;

import com.boki.realworld.api.article.exception.DuplicatedFavoriteException;
import com.boki.realworld.api.article.exception.InvalidUnFavoriteException;
import com.boki.realworld.api.tag.domain.Tag;
import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.exception.InvalidUserException;
import com.boki.realworld.common.BaseTimeEntity;
import com.boki.realworld.common.exception.IllegalParameterException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
@Table(name = "articles", indexes = {})
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Lob
    @Column(nullable = false)
    private String body;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToMany
    @JoinTable(name = "article_tags",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private final Set<Tag> tagList = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "article_favorites",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private final Set<User> favoriteList = new HashSet<>();

    @Builder
    private Article(LocalDateTime createdAt, LocalDateTime updatedAt,
        Long id, String slug, String title, String description, String body,
        User author) {
        super(createdAt, updatedAt);
        validateGenerateParams(title, description, body, author);
        this.id = id;
        this.slug = toSlugFrom(title);
        this.title = title;
        this.description = description;
        this.body = body;
        this.author = author;
    }

    private void validateGenerateParams(String title, String description, String body,
        User author) {
        if (!StringUtils.hasText(title) || !StringUtils.hasText(description)
            || !StringUtils.hasText(body) || ObjectUtils.isEmpty(author)) {
            throw new IllegalParameterException("article");
        }
    }

    public void update(String title, String description, String body) {
        if (StringUtils.hasText(title)) {
            this.title = title;
            this.slug = toSlugFrom(title);
        }
        this.description = description == null ? this.description : description;
        this.body = body == null ? this.body : body;
    }

    public void addTag(Tag tag) {
        tagList.add(tag);
    }

    public void favorite(User user) {
        validateNotMe(user);
        if (isFavorite(user)) {
            throw new DuplicatedFavoriteException();
        }
        favoriteList.add(user);
    }

    public void unFavorite(User user) {
        validateNotMe(user);
        if (!isFavorite(user)) {
            throw new InvalidUnFavoriteException();
        }
        favoriteList.remove(user);
    }

    private void validateNotMe(User user) {
        if (this.author == user) {
            throw new InvalidUserException("Your article can't like or be liked");
        }
    }

    public boolean isFavorite(User user) {
        return favoriteList.contains(user);
    }

    public int favoriteCount() {
        return favoriteList.size();
    }

    public static String toSlugFrom(String title) {
//        String title = "How! t?o train your dragon#";
        //한글유니코드(\uAC00-\uD7A3), 숫자 0~9(0-9), 영어 소문자a~z(a-z),
        // 대문자A~Z(A-Z), 공백(\s)을 제외한(^) 단어일 경우 체크
        return title.replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", "")
            .replace(" ", "-").toLowerCase();
    }
}