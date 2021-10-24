package com.example.springbootboard.controller;

import com.example.springbootboard.dto.ResponseDto;
import com.example.springbootboard.dto.UserDto;
import com.example.springbootboard.dto.request.RequestLoginUser;
import com.example.springbootboard.dto.request.RequestLogoutUser;
import com.example.springbootboard.dto.request.RequestSaveUser;
import com.example.springbootboard.dto.request.RequestUpdateUser;
import com.example.springbootboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@CrossOrigin(origins = "http://localhost:8081")
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8")
@RestController
public class UerApiController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Long> save(@Valid @RequestBody final RequestSaveUser request) {
        Long userId = userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto> getOne(@PathVariable("userId") final Long userId) {
        UserDto userDto = userService.findById(userId);

        return ResponseEntity.ok()
                .body(ResponseDto.builder()
                        .data(userDto)
                        .status(HttpStatus.OK)
                        .build());
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> update(@PathVariable("userId") final Long userId, @Valid @RequestBody final RequestUpdateUser request) {
        userService.update(userId, request);
        return ResponseEntity.ok()
                .location(URI.create("/api/v1/users/" + userId))
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody RequestLoginUser request) {

        Long userId = userService.login(request);
        return ResponseEntity.ok().body(userId);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RequestLogoutUser request) {
        userService.logout(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
