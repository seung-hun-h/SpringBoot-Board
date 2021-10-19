package com.example.springbootboard.dto.request;

import com.example.springbootboard.domain.Hobby;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestUpdateUser {

    private String name;
    private Hobby hobby;
    private Integer age;
    private String password;

    @Builder
    public RequestUpdateUser(String name, Hobby hobby, Integer age, String password) {
        this.name = name;
        this.hobby = hobby;
        this.age = age;
        this.password = password;
    }
}
