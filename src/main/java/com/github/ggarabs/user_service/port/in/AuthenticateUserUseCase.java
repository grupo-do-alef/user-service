package com.github.ggarabs.user_service.port.in;

public interface AuthenticateUserUseCase {
    String authenticate(AuthenticateUserCommand command);
}
