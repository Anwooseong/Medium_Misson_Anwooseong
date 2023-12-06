package com.ll.medium.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFormDto {

    @NotBlank(message = "이름을 입력해주세요.")
    private String username;

    @NotEmpty(message = "회원님의 로그인Id를 적어주세요.")
    @Pattern(
            regexp = "^(?=.*[a-z])[a-z0-9]{6,20}$",
            message = "id는 소문자 하나이상있어야하고, 6자~20자여야합니다."
    )
    private String loginId;

    @NotBlank(message = "회원님의 비밀번호를 적어주세요.")
    @Pattern(
            regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "영문자(A-Z, a-z), 숫자, 특수문자로 구성된 8~16자입니다."
    )
    private String password;

}
