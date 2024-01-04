package com.ll.medium.domain.post.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostFormDto {

    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "내용를 적어주세요.")
    private String content;

    private Boolean checkPublic = false;

    private Boolean isPaid = false;

}
