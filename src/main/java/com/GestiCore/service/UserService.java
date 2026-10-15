package com.GestiCore.service;

import com.GestiCore.dto.UserRequestDto;
import com.GestiCore.dto.UserResponseDto;
import com.GestiCore.model.User;
import com.GestiCore.exception.DuplicateResourceException;
import com.GestiCore.exception.ResourceNotFoundException;
import com.GestiCore.mapper.UserMapper;
import com.GestiCore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDto create(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Email already used: " + dto.email());
        }
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        return userMapper.toDto(getUserOrThrow(id));
    }

    @Transactional
    public UserResponseDto update(Long id, UserRequestDto dto) {
        User user = getUserOrThrow(id);
        if (!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Email already used: " + dto.email());
        }
        userMapper.updateEntity(user, dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(getUserOrThrow(id));
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }
}