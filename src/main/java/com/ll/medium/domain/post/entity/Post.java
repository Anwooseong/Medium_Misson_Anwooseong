package com.ll.medium.domain.post.entity;

import com.ll.medium.domain.member.entity.Member;
import com.ll.medium.domain.post.dto.PostFormDto;
import com.ll.medium.global.jpa.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private int viewCount;
    private Boolean checkPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private boolean isPaid;

    public static Post toEntity(PostFormDto postFormDto, Member member) {
        return Post.builder()
                .title(postFormDto.getTitle())
                .content(postFormDto.getContent())
                .checkPublic(postFormDto.getCheckPublic())
                .isPaid(postFormDto.getIsPaid())
                .viewCount(0)
                .member(member)
                .build();
    }

    //메서드
    public void updateViewCount() {
        this.viewCount = this.viewCount + 1;
    }

    public void updatePost(PostFormDto postFormDto) {
        this.title = postFormDto.getTitle();
        this.content = postFormDto.getContent();
        this.checkPublic = postFormDto.getCheckPublic();
    }

}
