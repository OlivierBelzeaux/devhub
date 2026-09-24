package com.olivier.devhub.user.api;

import java.time.Instant;

public record LoginResponse(
        String accessToken,
        Instant expiresAt
) {
}
