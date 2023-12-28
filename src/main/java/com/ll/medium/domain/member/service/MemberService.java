package com.ll.medium.domain.member.service;

import com.ll.medium.domain.member.entity.Member;
import com.ll.medium.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> _findMember = memberRepository.findByLoginId(member.getLoginId());
        if (_findMember.isPresent()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        System.out.println("loginId = " + loginId);
        Optional<Member> _member = memberRepository.findByLoginId(loginId);

        if (_member == null) {
            throw new UsernameNotFoundException(loginId);
        }
        Member member = _member.get();
        System.out.println("member = " + member);
        return User.builder()
                .username(member.getLoginId())
                .password(member.getPassword())
                .authorities(member.getAuthorities())
                .build();
//        return new User(
//                member.getLoginId(),
//                member.getPassword(),
//                member.getAuthorities()
//        );
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
