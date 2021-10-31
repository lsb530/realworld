package com.boki.realworld.api.tag.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.boki.realworld.common.exception.IllegalParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TagTest {

    @DisplayName("[태그 생성] - 예외 발생")
    @Test
    void invalidCreateTag() {
        assertAll(
            () -> assertThrows(IllegalParameterException.class,
                () -> new Tag(null)),
            () -> assertThrows(IllegalParameterException.class,
                () -> new Tag("")),
            () -> assertThrows(IllegalParameterException.class,
                () -> new Tag(" ")),
            () -> assertThrows(IllegalParameterException.class,
                () -> new Tag("k"), "태그 생성 글자수는 2~15"),
            () -> assertThrows(IllegalParameterException.class,
                () -> new Tag("This is too long tag name"))
        );
    }

    @DisplayName("[태그 생성]")
    @Test
    void CreateTag() {
        String name = "Springboot";
        Tag tag = new Tag(name);
        assertEquals(name, tag.getName());
    }
}