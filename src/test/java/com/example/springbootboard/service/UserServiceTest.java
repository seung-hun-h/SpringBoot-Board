package com.example.springbootboard.service;

import com.example.springbootboard.domain.Hobby;
import com.example.springbootboard.domain.User;
import com.example.springbootboard.dto.UserDto;
import com.example.springbootboard.dto.request.RequestSaveUser;
import com.example.springbootboard.dto.request.RequestUpdateUser;
import com.example.springbootboard.error.exception.DuplicateEmailException;
import com.example.springbootboard.error.exception.EntityNotFoundException;
import com.example.springbootboard.error.exception.NotAllowedAccessException;
import com.example.springbootboard.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testNotNull() throws Exception {
        //given 
         
        //when 
         
        //then
        assertThat(userService).isNotNull();
    } 

    @Test 
    @DisplayName("사용자를 저장할 수 있다")
    public void testSave() throws Exception { 
        //given 
        RequestSaveUser request = RequestSaveUser.builder()
                .email("hello12@naver.com")
                .password("Hello123@@@@")
                .build();

        // when
        Long userId = userService.save(request);

        // then
        Optional<User> actual = userRepository.findById(userId);
        assertThat(actual).isPresent();
        assertThat(actual.get().getEmail()).isEqualTo(request.getEmail());
    }

    @Test
    @DisplayName("이메일은 중복될 수 없다.")
    public void testSaveDuplicateEmail() throws Exception {
        //given
        RequestSaveUser request = RequestSaveUser.builder()
                .email("hello12@naver.com")
                .password("Hello123@@@@")
                .build();

        userService.save(request);

        RequestSaveUser request2 = RequestSaveUser.builder()
                .email("hello12@naver.com")
                .password("Hello123@@@@")
                .build();

        // then
        assertThrows(DuplicateEmailException.class, () -> {
            // when
            userService.save(request2);
        });
    }

    @Test
    @DisplayName("사용자 ID로 단건 조회할 수 있다")
    public void testFindById() throws Exception {
        //given
        RequestSaveUser request = RequestSaveUser.builder()
                .email("hello12@naver.com")
                .password("Hello123@@@@")
                .build();
        // when
        Long userId = userService.save(request);
        
        // then
        UserDto dto = userService.findById(userId);
        assertThat(dto.getEmail()).isEqualTo(request.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 단건 조회할 수 없다")
    public void testFindByWrongId() throws Exception {
        //given
        RequestSaveUser request = RequestSaveUser.builder()
                .email("hello12@naver.com")
                .password("Hello123@@@@")
                .build();
        Long userId = userService.save(request);

        // then
        assertThrows(EntityNotFoundException.class, () -> {
            // when
            UserDto dto = userService.findById(userId + 1);
        });
    }
    
    @Test 
    @DisplayName("사용자 이메일로 단건 조회할 수 있다")
    public void testFindByEmail() throws Exception {
        //given
        RequestSaveUser request = RequestSaveUser.builder()
                .email("hello12@naver.com")
                .password("Hello123@@@@")
                .build();
        Long userId = userService.save(request);

        // when
        UserDto dto = userService.findByEmail(request.getEmail());

        // then
        assertThat(dto.getEmail()).isEqualTo(request.getEmail());
    }
    
    @Test 
    @DisplayName("존재하지 않는 사용자 이메일로 단건 조회할 수 없다")
    public void testFindByWrongEmail() throws Exception {
        //given
        RequestSaveUser request = RequestSaveUser.builder()
                .email("hello12@naver.com")
                .password("Hello123@@@@")
                .build();
        Long userId = userService.save(request);

        // then
        assertThrows(EntityNotFoundException.class, () -> {
            // when
            UserDto dto = userService.findByEmail("wrongEmail1@naver.com");
        });
    }

    @Test
    @DisplayName("로그인 할 수 있다")
    public void testLogin() throws Exception {
        //given
        User user = User.builder()
                .createdBy("admin")
                .createdAt(LocalDateTime.now())
                .password("passwordAbc123@")
                .email("email123@naver.com")
                .build();

        userRepository.save(user);

        //when
        userService.login(user.getEmail(), user.getPassword());

        //then
        assertThat(user.isLogin()).isTrue();
    }

    @Test
    @DisplayName("로그아웃 할 수 있다")
    public void testLogout() throws Exception {
        //given
        User user = User.builder()
                .createdBy("admin")
                .createdAt(LocalDateTime.now())
                .password("passwordAbc123@")
                .email("email123@naver.com")
                .build();

        userRepository.save(user);
        userService.login(user.getEmail(), user.getPassword());
        
        //when
        userService.logout(user.getEmail());

        //then
        assertThat(user.isLogin()).isFalse();
    }

    @Test
    @DisplayName("사용자 정보를 변경할 수 있다.")
    public void testUpdateUserInfo() throws Exception {
        //given
        RequestSaveUser request = RequestSaveUser.builder()
                .email("hello12@naver.com")
                .password("Hello123@@@@")
                .build();
        Long id = userService.save(request);

        userService.login(request.getEmail(), request.getPassword());

        RequestUpdateUser requestUpdate = RequestUpdateUser.builder()
                .name("name")
                .hobby(Hobby.SPORTS)
                .age(22)
                .password("updatedPaSsword!21")
                .build();

        // when
        Long userId = userService.update(id, requestUpdate);

        // then
        User user = userRepository.findById(userId).get();
        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getName()).isEqualTo(requestUpdate.getName());
        assertThat(user.getHobby()).isEqualTo(requestUpdate.getHobby());
        assertThat(user.getPassword()).isEqualTo(requestUpdate.getPassword());
    }
    
    @Test
    @DisplayName("로그인 하지 않은 사용자는 정보를 변경할 수 없다")
    public void testUpdateUserInfoNotLogin() throws Exception {
        //given
        RequestSaveUser request = RequestSaveUser.builder()
                .email("hello12@naver.com")
                .password("Hello123@@@@")
                .build();
        Long id = userService.save(request);


        RequestUpdateUser requestUpdate = RequestUpdateUser.builder()
                .name("name")
                .hobby(Hobby.SPORTS)
                .age(22)
                .password("updatedPaSsword!21")
                .build();

        // then

        assertThrows(NotAllowedAccessException.class, () -> {
            // when
            Long userId = userService.update(id, requestUpdate);
        });
    }
    
    @Test 
    @DisplayName("회원 탈퇴할 수 있다.")
    public void testDeleteUser() throws Exception {
        //given
        User user = User.builder()
                .createdBy("admin")
                .createdAt(LocalDateTime.now())
                .password("passwordAbc123@")
                .email("email123@naver.com")
                .build();

        userRepository.save(user);
        userService.login(user.getEmail(), user.getPassword());

        //when
        userService.deleteById(user.getId());

        //then
        Optional<User> actual = userRepository.findById(user.getId());
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("로그인 하지 않은 회원은 탈퇴할 수 없다")
    public void testDeleteUserNotLogin() throws Exception {
        //given
        User user = User.builder()
                .createdBy("admin")
                .createdAt(LocalDateTime.now())
                .password("passwordAbc123@")
                .email("email123@naver.com")
                .build();

        userRepository.save(user);

        //then
        assertThrows(NotAllowedAccessException.class, () -> {
            //when
            userService.deleteById(user.getId());
        });
    }
}