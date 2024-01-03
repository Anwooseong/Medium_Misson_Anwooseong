package com.ll.medium.global.initData;

import com.ll.medium.domain.member.entity.Member;
import com.ll.medium.domain.member.service.MemberService;
import com.ll.medium.domain.post.dto.PostFormDto;
import com.ll.medium.domain.post.entity.Post;
import com.ll.medium.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@Configuration
@Profile("!prod")
@Slf4j
@RequiredArgsConstructor
public class NotProd {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;

    @Bean
    @Order(3)
    public ApplicationRunner initNotProd() {
        return args -> {
            if (memberService.findByUsername("user1").isPresent()) return;

            Member memberUser1 = memberService.saveMember(new Member("user1", "user1", "user1@", true, passwordEncoder));
            Member memberUser2 = memberService.saveMember(new Member("user2", "user2", "user2@", true, passwordEncoder));
            Member memberUser3 = memberService.saveMember(new Member("user3", "user3", "user3@", true, passwordEncoder));
            Member memberUser4 = memberService.saveMember(new Member("user4", "user4", "user4@", true, passwordEncoder));

            //유료회원
            IntStream.rangeClosed(5, 100).forEach(i -> {
                memberService.saveMember(new Member("user"+i, "user"+i, "user"+i+"@", true, passwordEncoder));
            });

            IntStream.rangeClosed(101, 200).forEach(i -> {
                memberService.saveMember(new Member("user"+i, "user"+i, "user"+i+"@", false, passwordEncoder));
            });

            //memberUser1, 비공개글, 유료글
            IntStream.rangeClosed(1, 10).forEach(i -> {
                postRepository.save(Post.toEntity(new PostFormDto("test"+i, "test"+i, false, true), memberUser1));
            });

            //memberUser1, 공개글, 유료글
            IntStream.rangeClosed(11, 20).forEach(i -> {
                postRepository.save(Post.toEntity(new PostFormDto("test"+i, "test"+i, true, true), memberUser1));
            });

            //memberUser2, 비공개글, 유료글
            IntStream.rangeClosed(21, 80).forEach(i -> {
                postRepository.save(Post.toEntity(new PostFormDto("test"+i, "test"+i, false, true), memberUser2));
            });

            //memberUser3, 공개글, 유료글
            IntStream.rangeClosed(81, 100).forEach(i -> {
                postRepository.save(Post.toEntity(new PostFormDto("test"+i, "test"+i, true, true), memberUser3));
            });

            //memberUser4, 비공개글, 무료글
            IntStream.rangeClosed(101, 120).forEach(i -> {
                postRepository.save(Post.toEntity(new PostFormDto("test"+i, "test"+i, false, false), memberUser4));
            });
        };
    }
}
