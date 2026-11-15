package com.github.ggarabs.user_service.port.in;

import com.github.ggarabs.user_service.infra.rest.dto.UserResponse;

public interface RegisterUserUseCase {
    UserResponse register(RegisterUserCommand command);
}
