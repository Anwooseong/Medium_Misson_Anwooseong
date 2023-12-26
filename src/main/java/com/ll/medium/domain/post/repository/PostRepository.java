package com.ll.medium.domain.post.repository;

import com.ll.medium.domain.member.entity.Member;
import com.ll.medium.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.checkPublic = true ORDER BY p.createdDate DESC")
    Page<Post> findPublicPosts(Pageable pageable);

    Page<Post> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Page<Post> findAllByMemberOrderByCreatedDateDesc(Member member, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.checkPublic = true and p.member = :member ORDER BY p.createdDate DESC")
    Page<Post> findByMemberPublicOrderByCreatedDateDesc(@Param("member") Member member, Pageable pageable);
}
