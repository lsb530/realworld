package com.boki.realworld.api.tag.dto.response;

import com.boki.realworld.api.tag.domain.Tag;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SingleTagResponse {

    @JsonProperty("tag")
    private TagData tagResponse;

    public static SingleTagResponse of(Tag tag) {
        return new SingleTagResponse(TagData.of(tag));
    }

    @Getter
    @Builder
    public static class TagData {

        private String name;

        public static TagData of(Tag tag) {
            return TagData.builder()
                .name(tag.getName())
                .build();
        }

    }
}