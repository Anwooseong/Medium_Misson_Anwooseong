package com.ll.medium.domain.member.entity;

import com.ll.medium.domain.member.constants.Role;
import com.ll.medium.domain.member.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    public static Member toEntity(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(memberFormDto.getLoginId())
                .username(memberFormDto.getUsername())
                .password(passwordEncoder.encode(memberFormDto.getPassword()))
                .role(Role.USER)
                .build();
    }

}
