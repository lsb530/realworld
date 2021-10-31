package com.boki.realworld.fixture;

import com.boki.realworld.api.tag.domain.Tag;
import com.boki.realworld.api.tag.dto.request.MultipleTagRequest;
import java.util.List;

public class TagFixture {

    public static final Tag TAG1 = new Tag("Springboot");
    public static final Tag TAG2 = new Tag("NodeJs");
    public static final Tag TAG3 = new Tag("ReactJs");
    public static final MultipleTagRequest REQUEST =
        new MultipleTagRequest(List.of(
            new String[]{TAG1.getName(), TAG2.getName(), TAG3.getName()}
        ));
}