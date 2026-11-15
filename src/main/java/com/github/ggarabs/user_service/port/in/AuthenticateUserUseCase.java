package com.github.ggarabs.user_service.port.in;

import com.github.ggarabs.user_service.adapter.in.AuthController.LoginResponse;

public interface AuthenticateUserUseCase {
    LoginResponse authenticate(AuthenticateUserCommand command);
}
