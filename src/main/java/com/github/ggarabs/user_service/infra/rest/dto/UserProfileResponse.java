package com.github.ggarabs.user_service.infra.rest.dto;

import java.util.UUID;

public record UserProfileResponse(UUID id,
        String name,
        String phone,
        String email,
        String birthday,
        Double latitude,
        Double longitude,
        Double preferredRatio) {
}
