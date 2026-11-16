package com.github.ggarabs.user_service.port.in;

import java.util.UUID;

import com.github.ggarabs.user_service.infra.rest.dto.UserProfileResponse;

public interface GetUserProfileUseCase {
    UserProfileResponse getById(UUID id);
}
