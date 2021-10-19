package com.example.springbootboard.domain;

import com.example.springbootboard.error.exception.AuthenticationCredentialNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("User 이름은 null을 허용한다")
    public void testUserNameIsNull() throws Exception {
        User user = User.builder()
                .createdBy("admin")
                .createdAt(LocalDateTime.now())
                .email("email@naver.com")
                .password("1passworD")
                .name("admin")
                .build();
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"name.", "name*", "name!", "name&", "name@", "name#",
            "name%","name ","name*","name=","name+"
            })
    @DisplayName("User name은 _ 이외 다른 특수 문자를 허용하지 않는다")
    public void testUserNameSpecial(String name) throws Exception {
        //given

        //when

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            User.builder()
                    .name(name)
                    .age(27)
                    .hobby(Hobby.SPORTS)
                    .createdBy("createdBy")
                    .createdAt(LocalDateTime.now())
                    .email("email@naver.com")
                    .password("1passworD")
                    .build();
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"namE", "Name", "nAme", "NAME"})
    @DisplayName("User name은 대소문자를 구분한다")
    public void testUserNameCase(String inputName) throws Exception {
        //given 
        String lowerCase = "name";

        //when 
        User user = User.builder()
                .createdBy("createdBy")
                .createdAt(LocalDateTime.now())
                .name(inputName)
                .age(27)
                .hobby(Hobby.SPORTS)
                .email("email@naver.com")
                .password("1passworD")
                .build();
        //then
        assertThat(user.getName()).isNotEqualTo(lowerCase);
    }

    @Test
    @DisplayName("User의 나이는 0미만이 될 수 없다")
    public void testUserAgeUnderZero() throws Exception {
        //given
        int age = -1;

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            //when
            User.builder()
                    .createdBy("user")
                    .createdAt(LocalDateTime.now())
                    .name("name")
                    .age(age)
                    .hobby(Hobby.SPORTS)
                    .email("email@naver.com")
                    .password("1passworD")
                    .build();
        });
    }


    @Test
    @DisplayName("User의 나이는 nullable 하다")
    public void testUserAgeIsNullable() throws Exception {
        //given
        Integer age = null;

        //when
        User user = User.builder()
                .createdBy("user")
                .createdAt(LocalDateTime.now())
                .name("name")
                .age(age)
                .hobby(Hobby.SPORTS)
                .email("email@naver.com")
                .password("1passworD")
                .build();

        //then
        assertThat(user.getAge()).isNull();
    }

    @Test
    @DisplayName("User의 취미는 nullable 하다")
    public void testUserHobbyIsNullable() throws Exception {
        //given
        Hobby hobby = null;

        //when
        User user = User.builder()
                .createdBy("user")
                .createdAt(LocalDateTime.now())
                .name("name")
                .hobby(hobby)
                .email("email@naver.com")
                .password("1passworD")
                .build();

        //then
        assertThat(user.getHobby()).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"email1.com", "email1@", "@email.com"})
    @DisplayName("이메일은 RFC2822 standards를 따른다")
    public void testUserEmailInvalid(String email) throws Exception {
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            //when
            User user = User.builder()
                    .createdBy("createdBy")
                    .createdAt(LocalDateTime.now())
                    .email(email)
                    .password("1passworD")
                    .build();
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"1111111111", "aaaaaaaa11111111", "AAAAAAAA123123"})
    @DisplayName("사용자 비밀번호는 최소 1개의 소문자, 1개의 대문자, 1개의 숫자를 포함해야한다")
    public void testPasswordMinCondition(String password) throws Exception {
        //given
        String createdBy = "createdBy";
        LocalDateTime createdAt = LocalDateTime.now();
        String email = "aaa2@naver.com";

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            //when
            User user = User.builder()
                    .password(password)
                    .createdAt(createdAt)
                    .createdBy(createdBy)
                    .email(email)
                    .build();
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"123abcA@@@@@@", "123abcA@#$%^&*@", "123abcA(*)_/?.><"})
    @DisplayName("사용자 비밀번호는 특수문자가 허용된다.")
    public void testPasswordSpecialCharacter(String password) throws Exception {
        //given
        String createdBy = "createdBy";
        LocalDateTime createdAt = LocalDateTime.now();
        String email = "aaa2@naver.com";
        String name = "seunghun";
        //when
        User user = User.builder()
                .password(password)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .email(email)
                .name(name)
                .build();

        // then
        assertThat(user.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("사용자 비밀번호는 8자 이상 이어야한다")
    public void testPasswordUnderMinLength() throws Exception {
        //given
        String createdBy = "createdBy";
        LocalDateTime createdAt = LocalDateTime.now();
        String email = "aaa2@naver.com";
        String password = "1Ab$";

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            //when
            User user = User.builder()
                    .password(password)
                    .createdAt(createdAt)
                    .createdBy(createdBy)
                    .email(email)
                    .build();

        });
    }

    @Test
    @DisplayName("사용자 비밀번호는 30자 이하 이어야한다")
    public void testPasswordOverMaxLength() throws Exception {
        //given
        String createdBy = "createdBy";
        LocalDateTime createdAt = LocalDateTime.now();
        String email = "aaa2@naver.com";
        String password = "1Ab$".repeat(8);

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            //when
            User user = User.builder()
                    .password(password)
                    .createdAt(createdAt)
                    .createdBy(createdBy)
                    .email(email)
                    .build();

        });
    }

    @Test
    @DisplayName("사용자 비밀번호는 8자가 가능하다")
    public void testPasswordMinLength() throws Exception {
        //given
        String createdBy = "createdBy";
        LocalDateTime createdAt = LocalDateTime.now();
        String email = "aaa2@naver.com";
        String password = "1Ab$abdc";
        String name = "seunghun";

        //when
        User user = User.builder()
                .password(password)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .email(email)
                .name(name)
                .build();

        // then
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getPassword().length()).isEqualTo(8);
    }

    @Test
    @DisplayName("사용자 비밀번호는 30자가 가능하다")
    public void testPasswordMaxLength() throws Exception {
        //given
        String createdBy = "createdBy";
        LocalDateTime createdAt = LocalDateTime.now();
        String email = "aaa2@naver.com";
        String password = "1Ab$a".repeat(6);
        String name = "seunghun";

        //when
        User user = User.builder()
                .password(password)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .email(email)
                .name(name)
                .build();

        // then
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getPassword().length()).isEqualTo(30);
    }

    @Test
    @DisplayName("사용자 로그인")
    public void testLogin() throws Exception {
        //given
        String createdBy = "createdBy";
        LocalDateTime createdAt = LocalDateTime.now();
        String email = "aaa2@naver.com";
        String password = "1Ab$abdc";
        String name = "seunghun";

        User user = User.builder()
                .password(password)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .email(email)
                .name(name)
                .build();

        //when
        user.login(password);

        // then
        assertThat(user.isLogin()).isTrue();
    }

    @Test
    @DisplayName("이미 로그인된 사용자는 다시 로그인 할 수 없다")
    public void testLoginTwice() throws Exception {
        //given
        String createdBy = "createdBy";
        LocalDateTime createdAt = LocalDateTime.now();
        String email = "aaa2@naver.com";
        String password = "1Ab$abdc";
        String name = "name";

        User user = User.builder()
                .password(password)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .email(email)
                .name(name)
                .build();
        user.login(password);

        // then
        assertThrows(IllegalStateException.class, () -> {
            //when
            user.login(password);
        });
    }

    @Test
    @DisplayName("패스워드가 다른 사용자는 로그인 할 수 없다")
    public void testLoginDifferentPassword() throws Exception {
        //given
        String createdBy = "createdBy";
        LocalDateTime createdAt = LocalDateTime.now();
        String email = "aaa2@naver.com";
        String password = "1Ab$abdc";
        String name = "seunghun";

        User user = User.builder()
                .password(password)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .email(email)
                .name(name)
                .build();

        // then
        assertThrows(AuthenticationCredentialNotFoundException.class, () -> {
            //when
            user.login("AnotherPassword1");
        });
    }

    @Test
    @DisplayName("사용자 정보를 수정할 수 있다")
    public void testUserUpdate() throws Exception {
        //given
        String createdBy = "createdBy";
        LocalDateTime createdAt = LocalDateTime.now();
        String email = "aaa2@naver.com";
        String password = "1Ab$abdc";
        String name = "name";

        User user = User.builder()
                .password(password)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .email(email)
                .name(name)
                .build();

        user.login(password);
        //when
        user.update("updatedName", 20, Hobby.SPORTS, "updatedPassword1@");

        // then
        assertThat(user.getPassword()).isEqualTo("updatedPassword1@");
        assertThat(user.getName()).isEqualTo("updatedName");
        assertThat(user.getAge()).isEqualTo(20);
        assertThat(user.getHobby()).isEqualTo(Hobby.SPORTS);
    }

    @Test
    @DisplayName("로그인 하지 않은 사용자는 정보를 수정할 수 없다.")
    public void testUserUpdateNotLoggedIn() throws Exception {
        //given
        String createdBy = "createdBy";
        LocalDateTime createdAt = LocalDateTime.now();
        String email = "aaa2@naver.com";
        String password = "1Ab$abdc";
        String name = "name";

        User user = User.builder()
                .password(password)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .email(email)
                .name(name)
                .build();

        // then
        assertThrows(RuntimeException.class, () -> {
            //when
            user.update("updatedName", 20, Hobby.SPORTS, "updatedPassword1@");
        });

    }


}