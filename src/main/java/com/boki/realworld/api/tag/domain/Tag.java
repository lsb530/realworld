package com.boki.realworld.api.tag.domain;

import com.boki.realworld.common.BaseTimeEntity;
import com.boki.realworld.common.exception.IllegalParameterException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tags", indexes = {
    @Index(name = "idx_name", columnList = "name")
})
public class Tag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 2, max = 15, message = "tag name size should be between 2-15")
    private String name;

    public Tag(String name) {
        validateBuildParam(name);
        this.name = name;
    }

    @Builder
    private Tag(Long id, String name) {
        validateBuildParam(name);
        this.id = id;
        this.name = name;
    }

    private void validateBuildParam(String name) {
        if (!StringUtils.hasText(name) || (name.length() < 2 || name.length() > 15)) {
            throw new IllegalParameterException("tag");
        }
    }
}