package com.ll.medium.domain.member.entity;

import com.ll.medium.domain.member.constants.Role;
import com.ll.medium.domain.member.dto.MemberFormDto;
import com.ll.medium.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String loginId;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    List<Post> postList = new ArrayList<>();

    public static Member toEntity(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(memberFormDto.getLoginId())
                .username(memberFormDto.getUsername())
                .password(passwordEncoder.encode(memberFormDto.getPassword()))
                .role(Role.USER)
                .build();
    }

    //연관관계 메서드
    public void deletePost(Post post) {
//        this.postList.remove()
    }

}
