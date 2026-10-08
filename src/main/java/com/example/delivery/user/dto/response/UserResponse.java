package com.example.delivery.user.dto.response;

import com.example.delivery.user.entity.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponse {
    private Long id;

    private String loginId;

    private UserRole role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public UserResponse(Long id, String loginId, UserRole role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.loginId = loginId;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
