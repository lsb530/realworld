package com.boki.realworld.api.tag.dto.response;

import com.boki.realworld.api.tag.domain.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultipleTagResponse {

    private List<String> tags;

    public MultipleTagResponse(List<String> tags) {
        this.tags = tags;
    }

    public static MultipleTagResponse of(List<Tag> tags) {
        List<String> nameList = tags.stream().map(Tag::getName).collect(Collectors.toList());
        return new MultipleTagResponse(nameList);
    }
}