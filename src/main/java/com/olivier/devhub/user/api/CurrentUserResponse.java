package com.olivier.devhub.user.api;

import com.olivier.devhub.user.domain.UserRole;

import java.util.UUID;

public record CurrentUserResponse(
        UUID id,
        String email,
        UserRole role
) {
}
