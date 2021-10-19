package com.example.springbootboard.dto.request;

import com.example.springbootboard.domain.Title;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class RequestUpdatePost {

    @NotNull
    private Long userId;

    @Size(min = Title.TITLE_MIN_LENGTH, max = Title.TITLE_MAX_LENGTH, message = "게시물 제목의 길이를 확인해주세요")
    @NotBlank(message = "게시물 제목은 필수입니다")
    private String title;

    @NotNull(message = "게시물 내용은 필수입니다")
    private String content;

    @Builder
    public RequestUpdatePost(Long userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }
}
