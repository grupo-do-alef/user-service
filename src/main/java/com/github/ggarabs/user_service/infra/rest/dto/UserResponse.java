package com.github.ggarabs.user_service.infra.rest.dto;

import java.util.UUID;

public record UserResponse(
                UUID id,
                String name,
                String email) {
}
