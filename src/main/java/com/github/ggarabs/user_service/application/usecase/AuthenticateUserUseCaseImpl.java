package com.github.ggarabs.user_service.application.usecase;

import com.github.ggarabs.user_service.domain.model.User;
import com.github.ggarabs.user_service.port.in.AuthenticateUserCommand;
import com.github.ggarabs.user_service.port.in.AuthenticateUserUseCase;
import com.github.ggarabs.user_service.port.out.PasswordEncoderPort;
import com.github.ggarabs.user_service.port.out.TokenProviderPort;
import com.github.ggarabs.user_service.port.out.UserRepositoryPort;

public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;

    public AuthenticateUserUseCaseImpl(UserRepositoryPort userRepositoryPort,
            PasswordEncoderPort passwordEncoderPort,
            TokenProviderPort tokenProviderPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProviderPort = tokenProviderPort;
    }

    @Override
    public String authenticate(AuthenticateUserCommand command) {
        User user = userRepositoryPort.findByEmail(command.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoderPort.matches(command.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return tokenProviderPort.generateToken(user);
    }
}
