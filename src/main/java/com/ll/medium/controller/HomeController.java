package com.ll.medium.controller;

import com.ll.medium.domain.post.entity.Post;
import com.ll.medium.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String showMain(Model model) {
        // 현재 사용자의 인증 여부 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication.getPrincipal() != "anonymousUser" && authentication.isAuthenticated();
        System.out.println("isAuthenticated = " + isAuthenticated);

        PageRequest pageRequest = PageRequest.of(0, 30);

        // 사용자의 인증 상태에 따라 다른 메서드 호출
        Page<Post> posts;
        if (isAuthenticated) {
            posts = postService.getAllPosts(pageRequest);
        } else {
            posts = postService.getPublicPosts(pageRequest);
        }

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("posts", posts);

        return "domain/home/home/main";
    }
}
