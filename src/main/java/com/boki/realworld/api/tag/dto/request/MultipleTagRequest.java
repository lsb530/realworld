package com.boki.realworld.api.tag.dto.request;

import com.boki.realworld.api.tag.domain.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MultipleTagRequest {

    private List<String> tagList;

    public List<Tag> toEntities() {
        return tagList.stream().map(Tag::new)
            .collect(Collectors.toList());
    }
}