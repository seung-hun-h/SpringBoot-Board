package com.example.springbootboard.service;

import com.example.springbootboard.domain.User;
import com.example.springbootboard.dto.UserDto;
import com.example.springbootboard.dto.request.RequestSaveUser;
import com.example.springbootboard.dto.request.RequestUpdateUser;
import com.example.springbootboard.error.exception.DuplicateEmailException;
import com.example.springbootboard.error.exception.EntityNotFoundException;
import com.example.springbootboard.error.exception.ErrorCode;
import com.example.springbootboard.error.exception.NotAllowedAccessException;
import com.example.springbootboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long save(RequestSaveUser request) {

        if (checkEmailDuplicate(request.getEmail())) {
            throw new DuplicateEmailException(MessageFormat.format("Email is already existed. email = {0}", request.getEmail()));
        }

        User user = userRepository.save(request.toEntity());
        return user.getId();
    }

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("There is no user. id = {0}", userId)));

        return toDto(user);
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("There is no user. email = {0}", email)));
        return toDto(user);
    }

    private UserDto toDto(User user) {

        return UserDto.builder()
                .age(user.getAge())
                .name(user.getName())
                .hobby(user.getHobby())
                .email(user.getEmail())
                .build();

    }

    @Transactional
    public Long update(Long userId, RequestUpdateUser request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("There is no user. id = {0}", userId)));

        user.update(request.getName(), request.getAge(), request.getHobby(), request.getPassword());

        return userId;
    }

    @Transactional
    public void login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("There is no user. email = {0}", email)));

        user.login(password);
    }

    @Transactional
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("There is no user. email = {0}", email)));

        user.logout();
    }

    @Transactional
    public void deleteById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("There is no user. id = {0}", userId)));

        if (!user.isLogin())
            throw new NotAllowedAccessException(MessageFormat.format("User is not logged in. id = {0}", userId));

        userRepository.delete(user);
    }
}
