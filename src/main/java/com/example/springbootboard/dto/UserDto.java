package com.example.springbootboard.dto;

import com.example.springbootboard.domain.Hobby;
import com.example.springbootboard.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    @NotBlank
    private String email;
    private String name;
    private Integer age;
    private Hobby hobby;

    @Builder
    public UserDto(String email, String name, Integer age, Hobby hobby) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.hobby = hobby;
    }

    public User toEntity() {
        return User.builder()
                .createdAt(LocalDateTime.now())
                .createdBy(getName())
                .name(getName())
                .age(getAge())
                .hobby(getHobby())
                .build();
    }
}
