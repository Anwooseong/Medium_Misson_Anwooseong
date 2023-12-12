package com.ll.medium.domain.post.service;

import com.ll.medium.domain.member.entity.Member;
import com.ll.medium.domain.member.repository.MemberRepository;
import com.ll.medium.domain.post.dto.PostFormDto;
import com.ll.medium.domain.post.entity.Post;
import com.ll.medium.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Post findById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글입니다."));
    }

    public void savePost(PostFormDto postFormDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        Member findMember = memberRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        Post savePost = Post.toEntity(postFormDto, findMember);
        postRepository.save(savePost);
    }

    public Page<Post> getPublicPosts(PageRequest pageRequest) {
        return postRepository.findPublicPosts(pageRequest);
    }

    public Page<Post> getAllPosts(PageRequest pageRequest) {
        return postRepository.findAllByOrderByCreatedDateDesc(pageRequest);
    }

    public Page<Post> getAllOtherPublicPosts(Long memberId, PageRequest pageRequest) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        return postRepository.findByMemberPublicOrderByCreatedDateDesc(findMember, pageRequest);
    }

    public Page<Post> getAllOtherPosts(Long memberId, PageRequest pageRequest) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        return postRepository.findAllByMemberOrderByCreatedDateDesc(findMember, pageRequest);
    }

    public Post detailPost(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글입니다."));
        return findPost;
    }

    public void viewCount(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글입니다."));
        findPost.updateViewCount();
    }

    public void deletePost(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글입니다."));
        postRepository.delete(findPost);
    }

    public void editPost(Long postId, PostFormDto postFormDto) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글입니다."));
        findPost.updatePost(postFormDto);
    }

    public Page<Post> getMyPosts(Authentication authentication, PageRequest pageRequest) {
        String loginId = authentication.getName();
        Member findMember = memberRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        return postRepository.findAllByMemberOrderByCreatedDateDesc(findMember, pageRequest);
    }
}
