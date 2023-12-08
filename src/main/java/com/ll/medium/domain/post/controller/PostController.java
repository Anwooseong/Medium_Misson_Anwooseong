package com.ll.medium.domain.post.controller;

import com.ll.medium.domain.post.dto.PostFormDto;
import com.ll.medium.domain.post.entity.Post;
import com.ll.medium.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/write")
    public String createForm(PostFormDto postFormDto, Model model) {
        model.addAttribute("postFormDto", postFormDto);
        return "domain/post/createPostForm";
    }

    @PostMapping("/write")
    public String savePost(@Valid PostFormDto postFormDto, BindingResult bindingResult) {
        System.out.println("hello");
        if (bindingResult.hasErrors()) {
            return "domain/post/createPostForm";
        }
        postService.savePost(postFormDto);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String getPosts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        // Spring Security를 이용하여 사용자의 인증 상태 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication.getPrincipal() != "anonymousUser" && authentication.isAuthenticated();

        // 사용자의 인증 상태에 따라 다른 메서드 호출
        Page<Post> posts;
        if (isAuthenticated) {
            posts = postService.getAllPosts(pageRequest);
        } else {
            posts = postService.getPublicPosts(pageRequest);
        }

        model.addAttribute("posts", posts);
        return "domain/post/list";
    }

    @GetMapping("/{postId}")
    public String detailPost(@PathVariable Long postId, Model model) {
        // Spring Security를 이용하여 사용자의 인증 상태 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication.getPrincipal() != "anonymousUser" && authentication.isAuthenticated();
        Post post = postService.detailPost(postId);
        if (!post.getCheckPublic()) {
            if (!isAuthenticated) {
                //checkPublic이 false인데 익명유저일때
                model.addAttribute("isPossible", false);
            } else {
                //checkPublic이 false일때 인증유저일때
                model.addAttribute("isPossible", true);
            }
        } else {
            //checkPublic이 true일때
            model.addAttribute("isPossible", true);
        }
        model.addAttribute("isMyPost", authentication.getName().equals(post.getMember().getLoginId()));
        model.addAttribute("post", post);
        return "domain/post/detailPost";
    }

    @DeleteMapping("/{postId}/delete")
    @ResponseBody
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }
}
