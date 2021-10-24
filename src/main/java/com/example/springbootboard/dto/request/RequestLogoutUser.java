package com.example.springbootboard.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RequestLogoutUser {

    @NotBlank
    private String email;

    public RequestLogoutUser(String email) {

        this.email = email;
    }
}

