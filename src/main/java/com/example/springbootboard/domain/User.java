package com.example.springbootboard.domain;

import com.example.springbootboard.error.exception.NotAllowedAccessException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email"})
)
@Entity
public class User extends BaseEntity {

    private static final String nameRegex = "^[가-힣a-zA-Z0-9_]{1,30}$";
    /***
     * - at least 8 characters
     * - must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number
     * - Can contain special characters
     */
    private static final String passwordRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,30}$";

    /***
     * Email Validation as per RFC2822 standards.
     */
    private static final String emailRegex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "hobby")
    @Enumerated(EnumType.STRING)
    private Hobby hobby;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    private boolean login;

    @Builder
    public User(String createdBy, LocalDateTime createdAt, String name, Integer age, Hobby hobby, String email, String password) {
        super(createdBy, createdAt);

        validate(name, age, email, password);

        this.name = name;
        this.age = age;
        this.hobby = hobby;
        this.email = email;
        this.password = password;
        this.login = false;
    }
    
    //== 비즈니스 메서드 ==//
    public void update(String name, Integer age, Hobby hobby, String password) {
        validate(name, age, this.email, password);

        if (!isLogin())
            throw new NotAllowedAccessException(MessageFormat.format("User is not logged in. email = {0}", this.email));

        this.name = name;
        this.age = age;
        this.hobby = hobby;
        this.password = password;
    }

    public void login(String password) {
        validPassword(password);

        if (!this.password.equals(password)) {
            throw new IllegalArgumentException(MessageFormat.format("Wrong password. password = {0}", password));
        }

        if (this.isLogin()) {
            throw new IllegalStateException("User is already logged in");
        }

        this.login = true;
    }

    public void logout() {
        if (!isLogin())
            throw new NotAllowedAccessException(MessageFormat.format("User is not logged in. email = {0}", this.email));

        this.login = false;
    }

    //== 검증 메서드 ==//
    private void validate(String name, Integer age, String email, String password) {
        validName(name);
        validAge(age);
        validEmail(email);
        validPassword(password);

    }

    private void validPassword(String password) {

        Assert.notNull(password, "User password should not be null");

        if (!Pattern.matches(passwordRegex, password)) {
            throw new IllegalArgumentException(MessageFormat.format("Invalid User password. password = {0}", password));
        }
    }

    private void validEmail(String email) {

        Assert.notNull(email, "User email should not be null");

        if (!Pattern.matches(emailRegex, email)) {
            throw new IllegalArgumentException(MessageFormat.format("Invalid User email. email = {0}", email));
        }
    }

    private void validName(String name) {

        if (name == null)
            return;

        if (!Pattern.matches(nameRegex, name)) {
            throw new IllegalArgumentException(MessageFormat.format("Invalid User name. name = {0}", name));
        }
    }

    private void validAge(Integer age) {

        if (age == null)
            return;

        if (age < 0) {
            throw new IllegalArgumentException(MessageFormat.format("User age should be over 0. age = {0}", age));
        }
    }


}