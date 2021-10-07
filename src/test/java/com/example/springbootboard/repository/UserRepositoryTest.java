package com.example.springbootboard.repository;

import com.example.springbootboard.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Test
    @DisplayName("User가 저장된다")
    public void testSaveUser() throws Exception {
        //given
        User user = User.builder()
                .name("seunghun")
                .createdBy("createdBy")
                .createdAt(LocalDateTime.now())
                .build();
        //when
        userRepository.save(user);

        //then
        Optional<User> find = userRepository.findById(user.getId());

        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(find).isPresent();
        assertThat(find.get().getName()).isEqualTo(user.getName());
    }

    @Transactional
    @Test
    @DisplayName("User가 수정된다")
    public void testUpdateUser() throws Exception {
        //given
        User user = User.builder()
                .name("seunghun")
                .createdBy("createdBy")
                .createdAt(LocalDateTime.now())
                .build();
        User entity = userRepository.save(user);

        //when
        entity.update("update", user.getAge(), user.getHobby());

        //then
        Optional<User> find = userRepository.findById(user.getId());
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(find).isPresent();
        assertThat(find.get().getName()).isEqualTo(entity.getName());
    }

    @Transactional
    @Test
    @DisplayName("User가 삭제된다")
    public void testDeleteUser() throws Exception {
        //given
        User user = User.builder()
                .name("seunghun")
                .createdBy("createdBy")
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        //when
        userRepository.delete(user);

        //then
        Optional<User> find = userRepository.findById(user.getId());
        assertThat(userRepository.count()).isEqualTo(0);
        assertThat(find).isEmpty();
    }



}