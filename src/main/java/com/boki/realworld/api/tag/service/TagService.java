package com.boki.realworld.api.tag.service;

import com.boki.realworld.api.tag.domain.Tag;
import com.boki.realworld.api.tag.domain.TagRepository;
import com.boki.realworld.api.tag.dto.request.MultipleTagRequest;
import com.boki.realworld.api.tag.dto.response.MultipleTagResponse;
import com.boki.realworld.api.tag.dto.response.SingleTagResponse;
import com.boki.realworld.api.tag.exception.DuplicatedTagNameException;
import com.boki.realworld.api.tag.exception.TagNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {

    private final TagRepository tagRepository;

    public SingleTagResponse findSingleTag(String name) {
        Tag tag = tagRepository.findTagByName(name)
            .orElseThrow(TagNotFoundException::new);
        return SingleTagResponse.of(tag);
    }

    public MultipleTagResponse findAllTags() {
        return MultipleTagResponse.of(tagRepository.findAll());
    }

    @Transactional
    public SingleTagResponse saveTag(String name) {
        validateTagName(name);
        Tag tag = Tag.builder().name(name).build();
        return SingleTagResponse.of(tagRepository.save(tag));
    }

    @Transactional
    public MultipleTagResponse saveAllStringTags(MultipleTagRequest request) {
        request.getTagList().forEach(this::validateTagName);
        List<Tag> tags = request.toEntities();
        return MultipleTagResponse.of(tagRepository.saveAll(tags));
    }

    @Transactional
    public List<Tag> saveAndFindAll(List<String> tags) {
        return tags.stream().map(tag -> tagRepository.findTagByName(tag)
                .orElseGet(() -> tagRepository.save(new Tag(tag))))
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTag(String name) {
        Tag tag = tagRepository.findTagByName(name)
            .orElseThrow(TagNotFoundException::new);
        tagRepository.delete(tag);
    }

    private void validateTagName(String name) {
        if (tagRepository.existsTagByName(name)) {
            throw new DuplicatedTagNameException();
        }
    }

}