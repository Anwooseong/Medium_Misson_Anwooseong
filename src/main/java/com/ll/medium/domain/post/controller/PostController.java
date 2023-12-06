package com.ll.medium.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    @GetMapping("/write")
    public String createForm() {
        return "domain/post/createPostForm";
    }
}
