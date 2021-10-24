package com.example.springbootboard.dto.request;

import com.example.springbootboard.domain.Hobby;
import com.example.springbootboard.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RequestSaveUser {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    private Integer age;
    private String hobby;

    @Builder
    public RequestSaveUser(String email, String password, String name, Integer age, String hobby) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
        this.hobby = hobby;
    }

    public User toEntity() {
        return User.builder()
                .createdBy(getEmail())
                .createdAt(LocalDateTime.now())
                .name(getName())
                .age(getAge())
                .hobby(getHobby() == null ? null : Hobby.valueOf(getName()))
                .email(getEmail())
                .password(getPassword())
                .build();
    }
}
