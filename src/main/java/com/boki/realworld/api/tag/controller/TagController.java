package com.boki.realworld.api.tag.controller;

import com.boki.realworld.api.tag.dto.request.MultipleTagRequest;
import com.boki.realworld.api.tag.dto.response.MultipleTagResponse;
import com.boki.realworld.api.tag.dto.response.SingleTagResponse;
import com.boki.realworld.api.tag.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Tag"})
@RequiredArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class TagController {

    private final TagService tagService;

    @ApiOperation(value = "태그 검색", notes = "태그를 검색한다")
    @GetMapping("/tag/{name}")
    private ResponseEntity<SingleTagResponse> findTag(@PathVariable("name") String name) {
        return ResponseEntity.ok().body(tagService.findSingleTag(name));
    }

    @ApiOperation(value = "태그 저장", notes = "태그를 저장한다")
    @PostMapping("/tag/{name}")
    private ResponseEntity<SingleTagResponse> saveTag(@PathVariable("name") String name) {
        URI location = URI.create("/api/tag/" + name);
        return ResponseEntity.created(location).body(tagService.saveTag(name));
    }

    @ApiOperation(value = "태그 삭제", notes = "태그를 삭제한다")
    @DeleteMapping("/tag/{name}")
    private ResponseEntity<Void> deleteTag(@PathVariable("name") String name) {
        tagService.deleteTag(name);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "태그 전체 검색", notes = "태그를 모두 검색한다")
    @GetMapping("/tags")
    private ResponseEntity<MultipleTagResponse> findAllTags() {
        return ResponseEntity.ok().body(tagService.findAllTags());
    }

    @ApiOperation(value = "태그 여러개 저장", notes = "태그를 여러개 저장한다")
    @PostMapping("/tags")
    private ResponseEntity<MultipleTagResponse> saveAllTags(
        @RequestBody MultipleTagRequest request) {
        return ResponseEntity.ok().body(tagService.saveAllStringTags(request));
    }

}