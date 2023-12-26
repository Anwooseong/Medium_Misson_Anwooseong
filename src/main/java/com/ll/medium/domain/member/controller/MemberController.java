package com.ll.medium.domain.member.controller;

import com.ll.medium.domain.member.dto.MemberFormDto;
import com.ll.medium.domain.member.entity.Member;
import com.ll.medium.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Transactional
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public String createForm(MemberFormDto memberFormDto, Model model) {
        model.addAttribute("memberFormDto", memberFormDto);
        return "domain/members/createMemberForm";
    }

    @PostMapping("/join")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "domain/members/createMemberForm";
        }

        try {
            Member saveMember = Member.toEntity(memberFormDto, passwordEncoder);
            memberService.saveMember(saveMember);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "domain/members/createMemberForm";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "domain/members/loginMemberForm";
    }


    @GetMapping("/login/error")
    public String loginError(Model model) {

        model.addAttribute("loginErrorMessage", "아이디 또는 비밀번호를 입력해주세요");
        return "domain/members/loginMemberForm";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
}
