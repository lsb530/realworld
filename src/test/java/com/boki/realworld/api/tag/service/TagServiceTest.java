package com.boki.realworld.api.tag.service;

import static com.boki.realworld.fixture.TagFixture.REQUEST;
import static com.boki.realworld.fixture.TagFixture.TAG1;
import static com.boki.realworld.fixture.TagFixture.TAG2;
import static com.boki.realworld.fixture.TagFixture.TAG3;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.boki.realworld.api.tag.domain.Tag;
import com.boki.realworld.api.tag.domain.TagRepository;
import com.boki.realworld.api.tag.dto.response.SingleTagResponse.TagData;
import com.boki.realworld.api.tag.exception.DuplicatedTagNameException;
import com.boki.realworld.api.tag.exception.TagNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    private Tag tag;

    private List<Tag> tagList;

    @BeforeEach
    void setUp() {
        tagService = new TagService(tagRepository);
        tag = Tag.builder().name(TAG1.getName()).build();
        tagList = List.of(TAG1, TAG2, TAG3);
    }

    @DisplayName("[태그 조회] - 존재하지 않는 태그 조회 시 예외 발생")
    @Test
    void findNotFoundTag() {
        given(tagRepository.findTagByName(any())).willReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.findSingleTag(any()));
    }

    @DisplayName("[태그 조회]")
    @Test
    void findTag() {
        given(tagRepository.findTagByName(any())).willReturn(Optional.of(tag));
        TagData response = tagService.findSingleTag(TAG1.getName()).getTag();
        assertEquals(TAG1.getName(), response.getName());
    }

    @DisplayName("[태그 전체 조회]")
    @Test
    void findAllTags() {
        given(tagRepository.findAll()).willReturn(tagList);
        List<String> response = tagService.findAllTags().getTags();
        assertEquals(tagList.size(), response.size());
    }

    @DisplayName("[태그 단일 저장] - 이미 존재하는 태그면 예외 발생")
    @Test
    void saveTag_Duplicated() {
        given(tagRepository.existsTagByName(any())).willReturn(true);
        assertThrows(DuplicatedTagNameException.class,
            () -> tagService.saveTag(tag.getName()));
    }

    @DisplayName("[태그 단일 저장]")
    @Test
    void saveTag() {
        given(tagRepository.existsTagByName(any())).willReturn(false);
        given(tagRepository.save(any(Tag.class))).willReturn(tag);
        TagData response = tagService.saveTag(tag.getName()).getTag();
        assertEquals(tag.getName(), response.getName());
    }

    @DisplayName("[태그 여러개 저장]")
    @Test
    void saveTags() {
        given(tagRepository.existsTagByName(any())).willReturn(false);
        given(tagRepository.saveAll(any())).willReturn(tagList);
        List<String> response = tagService.saveAllStringTags(REQUEST).getTags();
        assertEquals(REQUEST.getTagList().size(), response.size());
    }
}