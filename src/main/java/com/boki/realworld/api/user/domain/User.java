package com.boki.realworld.api.user.domain;

import com.boki.realworld.api.user.exception.DuplicatedFollowException;
import com.boki.realworld.api.user.exception.InvalidUnFollowUserException;
import com.boki.realworld.api.user.exception.InvalidUserException;
import com.boki.realworld.common.BaseTimeEntity;
import com.boki.realworld.common.exception.IllegalParameterException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users"
    , indexes = {
    @Index(name = "i_email", columnList = "email"),
    @Index(name = "i_username", columnList = "username")
}
)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Transient
    private String token;

    @Lob
    private String bio;

    private String image;

    @ManyToMany
    @JoinTable(name = "followList",
        joinColumns = @JoinColumn(name = "FOLLOWING_ID"),
        inverseJoinColumns = @JoinColumn(name = "FOLLOWER_ID")
    )
    private final List<User> followList = new ArrayList<>();

    @Builder
    public User(Long id, String email, String username, String password, String token,
        String bio, String image) {
        validateBuildParams(email, username, password);
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.token = token;
        this.bio = bio;
        this.image = image;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void update(String email, String username, String password, String image, String bio) {
        this.email = ObjectUtils.isEmpty(email) ? this.email : email;
        this.username = ObjectUtils.isEmpty(username) ? this.username : username;
        this.password = ObjectUtils.isEmpty(password) ? this.password : password;
        this.image = image;
        this.bio = bio;
    }

    public void follow(User other) {
        validateNotMe(other);
        if (isFollow(other)) {
            throw new DuplicatedFollowException();
        }
        followList.add(other);
    }

    public void unfollow(User other) {
        validateNotMe(other);
        if (!isFollow(other)) {
            throw new InvalidUnFollowUserException();
        }
        followList.remove(other);
    }

    public void validateNotMe(User other) {
        if (this == other) {
            throw new InvalidUserException();
        }
    }

    public boolean isFollow(User other) {
        return followList.contains(other);
    }

    private void validateBuildParams(String email, String username, String password) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(username) || !StringUtils.hasText(
            password)) {
            throw new IllegalParameterException();
        }
    }

}