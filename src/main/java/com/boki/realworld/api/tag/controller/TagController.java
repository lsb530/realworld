package com.boki.realworld.api.tag.controller;

import com.boki.realworld.api.tag.dto.request.MultipleTagRequest;
import com.boki.realworld.api.tag.dto.response.MultipleTagResponse;
import com.boki.realworld.api.tag.dto.response.SingleTagResponse;
import com.boki.realworld.api.tag.service.TagService;
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

@RequiredArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class TagController {

    private final TagService tagService;

    @GetMapping("/tag/{name}")
    private ResponseEntity<SingleTagResponse> findTag(@PathVariable("name") String name) {
        return ResponseEntity.ok().body(tagService.findSingleTag(name));
    }

    @PostMapping("/tag/{name}")
    private ResponseEntity<SingleTagResponse> saveTag(@PathVariable("name") String name) {
        URI location = URI.create("/api/tag/" + name);
        return ResponseEntity.created(location).body(tagService.saveTag(name));
    }

    @DeleteMapping("/tag/{name}")
    private ResponseEntity<Void> deleteTag(@PathVariable("name") String name) {
        tagService.deleteTag(name);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tags")
    private ResponseEntity<MultipleTagResponse> findAllTags() {
        return ResponseEntity.ok().body(tagService.findAllTags());
    }

    @PostMapping("/tags")
    private ResponseEntity<MultipleTagResponse> saveAllTags(
        @RequestBody MultipleTagRequest request) {
        return ResponseEntity.ok().body(tagService.saveAllStringTags(request));
    }

}