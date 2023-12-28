package com.ll.medium.domain.post.controller;

import com.ll.medium.domain.post.entity.Post;
import com.ll.medium.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
@RequiredArgsConstructor
@RequestMapping("/b")
public class OtherPostController {

    private final PostService postService;

    @GetMapping("/{memberId}/list")
    public String otherPostList(
            @PathVariable Long memberId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "30") int size,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        // Spring Security를 이용하여 사용자의 인증 상태 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication.getPrincipal() != "anonymousUser" && authentication.isAuthenticated();

        // 사용자의 인증 상태에 따라 다른 메서드 호출
        Page<Post> posts;
        if (isAuthenticated) {
            posts = postService.getAllOtherPosts(memberId, pageRequest);
        } else {
            posts = postService.getAllOtherPublicPosts(memberId, pageRequest);
        }

        model.addAttribute("memberId", memberId);
        model.addAttribute("posts", posts);
        return "domain/post/otherList";
    }

    @GetMapping("/{memberId}/{postId}")
    public String detailPost(@PathVariable Long memberId, @PathVariable Long postId, Model model) {
        // Spring Security를 이용하여 사용자의 인증 상태 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication.getPrincipal() != "anonymousUser" && authentication.isAuthenticated();
        boolean authorityPaid = false;
        Post post = postService.detailPost(postId);
        //비공개
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

        if (post.isPaid()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                System.out.println("authority = " + authority);

                if (authority.toString().equals("ROLE_PAID")) {
                    System.out.println("hello");
                    authorityPaid = true;
                }
            }
        }

        model.addAttribute("isMyPost", authentication.getName().equals(post.getMember().getLoginId()));
        model.addAttribute("post", post);
        model.addAttribute("authorityPaid", authorityPaid);
        return "domain/post/detailPost";
    }
}
