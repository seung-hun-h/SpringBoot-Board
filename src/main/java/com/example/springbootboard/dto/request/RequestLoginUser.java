package com.example.springbootboard.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RequestLoginUser {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public RequestLoginUser(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
