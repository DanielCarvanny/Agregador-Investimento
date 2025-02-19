package tech.buildrun.agregadorinvestimentos.controller.dto;

import tech.buildrun.agregadorinvestimentos.entity.User;

import java.util.UUID;

public record UserResponseDTO(UUID userId, String username, String email) {
    public UserResponseDTO(User user) {
        this(user.getUserId(), user.getUsername(), user.getEmail());
    }
}

