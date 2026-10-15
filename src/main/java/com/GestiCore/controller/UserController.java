package com.GestiCore.controller;

import com.GestiCore.dto.UserRequestDto;
import com.GestiCore.dto.UserResponseDto;
import com.GestiCore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService  userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDto> getAll() {
        System.out.println("Getting all users");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable("id") Long id) {  // PathVariable("id") it take id from the url
        return userService.findById(id);
    }
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable("id") Long id, @Valid @RequestBody UserRequestDto dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}