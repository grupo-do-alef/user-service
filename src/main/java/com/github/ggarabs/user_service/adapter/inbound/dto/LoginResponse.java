package com.github.ggarabs.user_service.adapter.inbound.dto;

import java.util.UUID;

public record LoginResponse(UUID id, String email, String role, String message) {
}
