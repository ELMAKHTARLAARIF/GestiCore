package com.GestiCore.dto;

import java.time.LocalDateTime;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String role,
        LocalDateTime createdAt
) { }