package com.ll.medium.domain.member.entity;

import com.ll.medium.domain.member.dto.MemberFormDto;
import com.ll.medium.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
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

    private boolean isPaid;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    List<Post> postList = new ArrayList<>();

    public static Member toEntity(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(memberFormDto.getLoginId())
                .username(memberFormDto.getUsername())
                .password(passwordEncoder.encode(memberFormDto.getPassword()))
                .build();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        if (isPaid) {
            authorities.add(new SimpleGrantedAuthority("ROLE_PAID"));
        }

        if (List.of("system", "admin").contains(username)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return authorities;
    }

    //연관관계 메서드
    public void deletePost(Post post) {
//        this.postList.remove()
    }

}
